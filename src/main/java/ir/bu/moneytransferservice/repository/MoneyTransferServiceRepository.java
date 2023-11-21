package ir.bu.moneytransferservice.repository;

import ir.bu.moneytransferservice.model.ClientCard;
import ir.bu.moneytransferservice.model.ConfirmOperation;
import ir.bu.moneytransferservice.model.Operation;
import ir.bu.moneytransferservice.model.dto.OperationDtoForTransfer;

import java.util.Map;

public interface MoneyTransferServiceRepository {
    Operation createTransfer(OperationDtoForTransfer operationDtoForTransfer);

    void cardInit(ClientCard card);

    Operation getById(String id);

    boolean update(Operation operation);

    boolean confirmClientCardsDB(ClientCard card);

    ClientCard getCardFromByNumber(String number);

    String getCardToByNumber(String number);

    ConfirmOperation confirmOperation(Operation operation);

    //  boolean operationIsConfirm(Operation operation);
    Map<String, Operation> getByCardSender(ClientCard card);

    Map<String, Operation> getByCardRecipient(String cardOfRecipient);

    Map<String, Operation> getOperationsInIntervalId(String idStart, String idEnd);
}
