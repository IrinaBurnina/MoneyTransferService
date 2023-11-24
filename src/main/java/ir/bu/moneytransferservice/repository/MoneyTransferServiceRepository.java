package ir.bu.moneytransferservice.repository;

import ir.bu.moneytransferservice.model.ClientCard;
import ir.bu.moneytransferservice.model.ConfirmOperation;
import ir.bu.moneytransferservice.model.Operation;
import ir.bu.moneytransferservice.model.dto.OperationDtoForTransfer;

import java.util.Map;
import java.util.Optional;

public interface MoneyTransferServiceRepository {
    static Map<String, Operation> operations = null;

    Operation createTransfer(OperationDtoForTransfer operationDtoForTransfer);

    void cardInit(ClientCard card);

    Optional<Operation> getOperationById(String id);

    boolean update(Operation operation);

    void updateClientCard(ClientCard card);

    ClientCard getClientCardByNumber(String number);

    ClientCard getCardFromByIdOperation(String id);

    ClientCard getCardToByIdOperation(String id);

    Map<String, Operation> getOperations();

    Map<String, ClientCard> getClientCards();

    ConfirmOperation confirmOperation(Operation operation);

    Map<String, Operation> getByCardSender(ClientCard card);

    Map<String, Operation> getByCardRecipient(String cardOfRecipient);

    Map<String, Operation> getOperationsInIntervalId(String idStart, String idEnd);
}
