package ir.bu.moneytransferservice.service;

import ir.bu.moneytransferservice.exception.*;
import ir.bu.moneytransferservice.logger.Log;
import ir.bu.moneytransferservice.model.Amount;
import ir.bu.moneytransferservice.model.ClientCard;
import ir.bu.moneytransferservice.model.Operation;
import ir.bu.moneytransferservice.model.OperationStatus;
import ir.bu.moneytransferservice.model.dto.OperationDtoForConfirm;
import ir.bu.moneytransferservice.model.dto.OperationDtoForTransfer;
import ir.bu.moneytransferservice.model.dto.OperationDtoResponses;
import ir.bu.moneytransferservice.repository.MoneyTransferServiceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MoneyTransServiceServiceImpl implements MoneyTransferServiceService {
    private final MoneyTransferServiceRepository repository;
    private final Log logger;
    @Value("${service.fee}")
    private int fee;

    public MoneyTransServiceServiceImpl(MoneyTransferServiceRepository repository, Log logger) {
        this.repository = repository;
        this.logger = logger;

    }

    @Override
    public OperationDtoResponses transfer(OperationDtoForTransfer operationDtoForTransfer) {
        ClientCard cardFrom = repository.getClientCardByNumber(operationDtoForTransfer.getCardFromNumber());
        if (isEmpty(operationDtoForTransfer.getCardFromNumber()) ||
                isEmpty(operationDtoForTransfer.getCardFromValidTill()) ||
                isEmpty(operationDtoForTransfer.getCardFromCVV()) ||
                isEmpty(operationDtoForTransfer.getCardToNumber())) {
            String msg = "Some card parameter (number or date valid till or code CVV) is empty";
            logger.log(ErrorsForMoneyTransferService.INVALID_INPUT_DATA, msg);
            throw new InvalidInputData(msg);
        }
        if (operationDtoForTransfer.getAmount().getValue() == 0) {
            String msg = "Minimal value should be more then 0";
            logger.log(ErrorsForMoneyTransferService.INVALID_INPUT_DATA, msg);
            throw new InvalidInputData(msg);
        }
        if (cardFrom == null) {
            String msg = "Card FROM is not found";
            logger.log(ErrorsForMoneyTransferService.NOT_FOUND_EXCEPTION, msg);
            throw new NotFoundException(msg);
        }
        if (!cardFrom.getValidTill().equals(operationDtoForTransfer.getCardFromValidTill()) ||
                !cardFrom.getCodeCvv().equals(operationDtoForTransfer.getCardFromCVV()) ||
                !cardFrom.getNumber().equals(operationDtoForTransfer.getCardFromNumber())) {
            String msg = "Unknown input data (card number or date valid till or code CVV)";
            logger.log(ErrorsForMoneyTransferService.INVALID_INPUT_DATA, msg);
            throw new InvalidInputData(msg);
        }
        if (operationDtoForTransfer.getCardToNumber() == null) {
            String msg = "Card TO is not found";
            logger.log(ErrorsForMoneyTransferService.NOT_FOUND_EXCEPTION, msg);
            throw new NotFoundException(msg);
        }
        if (operationDtoForTransfer.getAmount().getValue() > cardFrom.getBalance()) {
            String msg = "Not enough money";
            logger.log(ErrorsForMoneyTransferService.EXPENSE_OVER_BALANCE, msg);
            throw new ExpenseOverBalance(msg);
        }
        Operation newOperation = repository.createTransfer(operationDtoForTransfer);
        logger.log(newOperation, "Input data are valid. Operation will be completed after confirming ");
        withSetVerificationCode(newOperation);
        repository.update(newOperation);
        return new OperationDtoResponses(newOperation.getId());
    }

    @Override
    public OperationDtoResponses confirm(OperationDtoForConfirm operationDtoForConfirm) {
        Operation operation;
        if (repository.getOperationById(operationDtoForConfirm.getOperationId()).isPresent()) {
            operation = repository.getOperationById(operationDtoForConfirm.getOperationId()).orElseThrow(NotFoundException::new);
            ClientCard clientCardFrom = repository.getCardFromByIdOperation(operationDtoForConfirm.getOperationId());
            ClientCard clientCardTo = repository.getCardToByIdOperation(operationDtoForConfirm.getOperationId());
            if (!operationDtoForConfirm.getCode().equals(operation.getVerificationCode())) {
                updateOperation(repository.getOperationById(operationDtoForConfirm.getOperationId()).get(), OperationStatus.ERROR_INPUT_DATA);
                String msg = "Operation isn't confirming";
                logger.log(operationDtoForConfirm, msg);
                throw new NonConfirmOperation(msg);
            } else {
                int changingOfBalances = operation.getAmount().getValue() + sumOfFee(operation).getValue();
                int newBalanceCardFrom = operation.getCardFromNumber().getBalance() - changingOfBalances;
                int newBalanceCardTo = repository.getClientCardByNumber(operation.getCardToNumber()).getBalance()
                        + operation.getAmount().getValue();
                clientCardFrom.setBalance(newBalanceCardFrom);
                clientCardTo.setBalance(newBalanceCardTo);
                repository.updateClientCard(clientCardFrom);
                repository.updateClientCard(clientCardTo);
                updateOperation(operation, OperationStatus.SUCCESS_TRANSFER);
                logger.log(operation, "Operation is completed");
                return new OperationDtoResponses(operationDtoForConfirm.getOperationId());
            }
        }
        String msg = "Operation isn't created";
        logger.log(operationDtoForConfirm, msg);
        throw new NotFoundException(msg);
    }

    private boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public void withSetVerificationCode(Operation operation) {
        operation.setVerificationCode("0000");
        repository.update(operation);
    }

    private void updateOperation(Operation operation, OperationStatus status) {
        operation.setOperationStatus(status);
        repository.update(operation);
    }

    public Amount sumOfFee(Operation operation) {
        int fee = operation.getAmount().getValue() * this.fee / 100;
        Amount amountOfFee = new Amount(operation.getAmount().getCurrency(), fee);
        operation.setFee(amountOfFee);
        repository.update(operation);
        return amountOfFee;

    }

    @Override
    public String toString() {
        return "MoneyTransServiceServiceImpl{" +
                "repository=" + repository +
                ", logger=" + logger +
                ", fee=" + fee +
                '}';
    }
}
