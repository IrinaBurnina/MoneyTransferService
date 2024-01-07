package ir.bu.moneytransferservice.service;

import ir.bu.moneytransferservice.dto.OperationDtoForConfirm;
import ir.bu.moneytransferservice.dto.OperationDtoForTransfer;
import ir.bu.moneytransferservice.dto.OperationDtoResponses;
import ir.bu.moneytransferservice.logger.Log;
import ir.bu.moneytransferservice.model.Amount;
import ir.bu.moneytransferservice.model.ClientCard;
import ir.bu.moneytransferservice.model.Operation;
import ir.bu.moneytransferservice.model.OperationStatus;
import ir.bu.moneytransferservice.repository.MoneyTransferServiceRepository;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

@Service
@ToString
@AllArgsConstructor
public class MoneyTransServiceServiceImpl implements MoneyTransferServiceService {
    private final MoneyTransferServiceRepository repository;
    private final Log logger;
    private final ThrowingExceptionsService exceptionsServ;

    @Override
    public OperationDtoResponses transfer(OperationDtoForTransfer operationDtoForTransfer) {
        if (checksOfTransfer(operationDtoForTransfer)) {
            Operation newOperation = repository.createTransfer(operationDtoForTransfer);
            logger.log(newOperation, "Input data are valid. Operation will be completed after confirming ");
            withSetVerificationCode(newOperation);
            repository.update(newOperation);
            return new OperationDtoResponses(newOperation.getId());
        }
        return null;
    }

    @Override
    public OperationDtoResponses confirm(OperationDtoForConfirm operationDtoForConfirm) {
        String id = operationDtoForConfirm.operationId();
        Operation operation = exceptionsServ.checkNotFoundOperation(id, logger, repository);
        if (exceptionsServ.checkConfirmCode(operationDtoForConfirm, operation, repository, logger)) {
            return writeNewOperationSettings(operation);
        }
        return null;
    }

    public void withSetVerificationCode(Operation operation) {
        operation.setVerificationCode("0000");
        repository.update(operation);
    }

    public OperationDtoResponses writeNewOperationSettings(Operation operation) {
        setNewBalances(operation);
        operation.setOperationStatus(OperationStatus.SUCCESS_TRANSFER);
        repository.update(operation);
        logger.log(operation, "Operation is completed");
        return new OperationDtoResponses(operation.getId());

    }

    public Amount calculateOfFee(Operation operation) {
        Amount amount = repository.calculateFee(operation.getAmount());
        operation.setFee(amount);
        repository.update(operation);
        return amount;
    }

    public ClientCard getCardByNumber(String number) {
        return repository.getClientCardByNumber(number);
    }

    public ClientCard getCardFromById(String id) {
        return repository.getCardFromByIdOperation(id);
    }

    public ClientCard getCardToById(String id) {
        return repository.getCardToByIdOperation(id);
    }

    public boolean checksOfTransfer(OperationDtoForTransfer operationDtoForTransfer) {
        if (exceptionsServ.checkEmptyFields(operationDtoForTransfer, logger) ||
                exceptionsServ.checkMinimalAmount(operationDtoForTransfer.getAmount(), logger) ||
                exceptionsServ.checkClientCard(getCardByNumber(operationDtoForTransfer.getCardFromNumber()),
                        logger, "Card FROM is not found") ||
                exceptionsServ.checkIncorrectData(getCardByNumber(operationDtoForTransfer.getCardFromNumber()),
                        operationDtoForTransfer, logger) ||
                exceptionsServ.checkClientCard(getCardByNumber(operationDtoForTransfer.getCardToNumber()),
                        logger, "Card TO is not found") ||
                exceptionsServ.checkEnoughMoney(getCardByNumber(operationDtoForTransfer.getCardFromNumber()),
                        operationDtoForTransfer, logger)) {
            return true;
        } else return false;
    }


    public int calculateNewBalanceCardFrom(Operation operation) {
        return operation.getCardFromNumber().getBalance() - calculateAmountOfTransfer(operation);
    }

    public int calculateAmountOfTransfer(Operation operation) {
        return operation.getAmount().value() + calculateOfFee(operation).value();
    }

    public int calculateNewBalanceCardTo(Operation operation) {
        return repository.getClientCardByNumber(operation.getCardToNumber()).getBalance()
                + operation.getAmount().value();
    }

    public void setNewBalances(Operation operation) {
        ClientCard cardFrom = operation.getCardFromNumber();
        ClientCard cardTo = getCardToById(operation.getId());
        cardFrom.setBalance(calculateNewBalanceCardFrom(operation));
        cardTo.setBalance(calculateNewBalanceCardTo(operation));
        repository.updateClientCard(cardFrom);
        repository.updateClientCard(cardTo);
    }
}
