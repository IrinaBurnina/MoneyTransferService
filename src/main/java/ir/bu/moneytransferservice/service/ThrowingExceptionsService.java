package ir.bu.moneytransferservice.service;

import ir.bu.moneytransferservice.dto.OperationDtoForConfirm;
import ir.bu.moneytransferservice.dto.OperationDtoForTransfer;
import ir.bu.moneytransferservice.dto.OperationDtoResponses;
import ir.bu.moneytransferservice.exception.ExpenseOverBalance;
import ir.bu.moneytransferservice.exception.InvalidInputData;
import ir.bu.moneytransferservice.exception.NonConfirmOperation;
import ir.bu.moneytransferservice.exception.NotFoundException;
import ir.bu.moneytransferservice.exception.errors.ErrorsForMoneyTransferService;
import ir.bu.moneytransferservice.logger.Log;
import ir.bu.moneytransferservice.model.Amount;
import ir.bu.moneytransferservice.model.ClientCard;
import ir.bu.moneytransferservice.model.Operation;
import ir.bu.moneytransferservice.model.OperationStatus;
import ir.bu.moneytransferservice.repository.MoneyTransferServiceRepository;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor
public class ThrowingExceptionsService {
    public boolean checkEmptyFields(OperationDtoForTransfer operationDtoForTransfer, Log logger) {
        if (isEmpty(operationDtoForTransfer.getCardFromNumber()) ||
                isEmpty(operationDtoForTransfer.getCardFromValidTill()) ||
                isEmpty(operationDtoForTransfer.getCardFromCVV()) ||
                isEmpty(operationDtoForTransfer.getCardToNumber())) {
            String msg = "Some card parameter (number or date valid till or code CVV) is empty";
            logger.log(ErrorsForMoneyTransferService.INVALID_INPUT_DATA, msg);
            throw new InvalidInputData(msg);
        }
        return true;
    }

    public boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public boolean checkMinimalAmount(Amount amount, Log logger) {
        if (amount.value() == 0) {
            String msg = "Minimal value should be more then 0";
            logger.log(ErrorsForMoneyTransferService.INVALID_INPUT_DATA, msg);
            throw new InvalidInputData(msg);
        }
        return true;
    }

    public boolean checkClientCard(ClientCard clientCard,
                                   Log logger, String toOrFrom) {
        if (clientCard == null) {
            String msg = "Card " + toOrFrom + " is not found";
            logger.log(ErrorsForMoneyTransferService.NOT_FOUND_EXCEPTION, msg);
            throw new NotFoundException(msg);
        }
        return true;
    }

    public boolean checkIncorrectData(ClientCard cardFrom,
                                      OperationDtoForTransfer operationDtoForTransfer,
                                      Log logger) {
        if (!cardFrom.getValidTill().equals(operationDtoForTransfer.getCardFromValidTill()) ||
                !cardFrom.getCodeCvv().equals(operationDtoForTransfer.getCardFromCVV()) ||
                !cardFrom.getNumber().equals(operationDtoForTransfer.getCardFromNumber())) {
            String msg = "Unknown input data (card number or date valid till or code CVV)";
            logger.log(ErrorsForMoneyTransferService.INVALID_INPUT_DATA, msg);
            throw new InvalidInputData(msg);
        }
        return true;
    }

    public boolean checkEnoughMoney(ClientCard cardFrom,
                                    OperationDtoForTransfer operationDtoForTransfer,
                                    Log logger) {
        if (operationDtoForTransfer.getAmount().value() > cardFrom.getBalance()) {
            String msg = "Not enough money";
            logger.log(ErrorsForMoneyTransferService.EXPENSE_OVER_BALANCE, msg);
            throw new ExpenseOverBalance(msg);
        }
        return true;
    }

    public boolean checkConfirmCode(OperationDtoForConfirm operationDtoForConfirm,
                                    Operation operation,
                                    MoneyTransferServiceRepository repository,
                                    Log logger) {
        if (!operationDtoForConfirm.code().equals(operation.getVerificationCode())) {
            operation.setOperationStatus(OperationStatus.ERROR_INPUT_DATA);
            repository.update(operation);
            String msg = "Operation isn't confirming";
            logger.log(operationDtoForConfirm, msg);
            throw new NonConfirmOperation(msg);
        }
        return true;
    }

    public Operation checkNotFoundOperation(String operationId, Log logger, MoneyTransferServiceRepository repository) {
        Optional<Operation> optionalOperation = repository.getOperationById(operationId);
        if (!(optionalOperation).isPresent()) {
            String msg = "Operation isn't created";
            logger.log(new OperationDtoResponses(operationId), msg);
            throw new NotFoundException(msg);
        }
        return optionalOperation.get();
    }
}
