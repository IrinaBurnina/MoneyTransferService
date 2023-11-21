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
        ClientCard cardFrom = repository.getCardFromByNumber(operationDtoForTransfer.getCardFromNumber());
        String cardTo = repository.getCardToByNumber(operationDtoForTransfer.getCardToNumber());
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
        if (!operationDtoForTransfer.getAmount().getCurrency().equals(cardFrom.getCurrency()) ||
                !operationDtoForTransfer.getAmount().getCurrency().equals(repository.getCardFromByNumber(cardTo).getCurrency())) {
            String msg = "Choosing currency is not available for these bank card";
            logger.log(ErrorsForMoneyTransferService.INVALID_INPUT_DATA, msg);
            throw new InvalidInputData("Choosing currency is not available for these bank card");
        }
        if (!cardFrom.getValidTill().equals(operationDtoForTransfer.getCardFromValidTill()) ||
                !cardFrom.getCodeCvv().equals(operationDtoForTransfer.getCardFromCVV()) ||
                !cardFrom.getNumber().equals(operationDtoForTransfer.getCardFromNumber())
                || !cardTo.equals(operationDtoForTransfer.getCardToNumber())) {
            String msg = "Unknown input data (card number or date valid till or code CVV)";
            logger.log(ErrorsForMoneyTransferService.INVALID_INPUT_DATA, msg);
            throw new InvalidInputData(msg);
        }
        if (cardFrom == null) {
            String msg = "Card FROM is not found";
            logger.log(ErrorsForMoneyTransferService.NOT_FOUND_EXCEPTION, msg);
            throw new NotFoundException(msg);
        }
        if (cardTo == null) {
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
        Operation withSetVerificationCodeOperation = withSetVerificationCode(newOperation);
        return new OperationDtoResponses(withSetVerificationCodeOperation.getId());
    }

    @Override
    public OperationDtoResponses confirm(OperationDtoForConfirm operationDtoForConfirm) {
        var operation = repository.getById(operationDtoForConfirm.getOperationId());
        if (operation == null) {
            String msg = "Operation isn't created";
            logger.log(operation, msg);
            throw new NotFoundException(msg);
        } else if (!operationDtoForConfirm.getVerificationCode().equals(operation.getVerificationCode())) {
            updateOperation(operation, OperationStatus.ERROR_INPUT_DATA);
            String msg = "Operation isn't confirming";
            logger.log(operation, msg);
            throw new NonConfirmOperation(msg);
        } else {
            int newBalance = operation.getCardFromNumber().getBalance() - operation.getAmount().getValue() - sumOfFee(operation).getValue();
            String cardNumber = operation.getCardFromNumber().getNumber();
            ClientCard clientCard = repository.getCardFromByNumber(cardNumber);
            clientCard.setBalance(newBalance);
            updateOperation(operation, OperationStatus.SUCCESS_TRANSFER);
            return new OperationDtoResponses(operation.getId());
        }
    }

    private boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public Operation withSetVerificationCode(Operation operation) {
        operation.setVerificationCode("0000");
        return operation;
    }

    private void updateOperation(Operation operation, OperationStatus status) {
        operation.setOperationStatus(status);
        repository.update(operation);
        logger.log(operation, "Operation is completed");
    }

    public Amount sumOfFee(Operation operation) {
        int fee = operation.getAmount().getValue() * this.fee / 100;
        Amount amountOfFee = new Amount(fee, operation.getAmount().getCurrency());
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
