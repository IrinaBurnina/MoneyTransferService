package ir.bu.moneytransferservice.repository;

import ir.bu.moneytransferservice.confirmTransfer.VerificationCode;
import ir.bu.moneytransferservice.exception.AttemptOfRepeatOperation;
import ir.bu.moneytransferservice.model.*;
import ir.bu.moneytransferservice.model.dto.OperationDtoForTransfer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MoneyTransServRepositoryImpl implements MoneyTransferServiceRepository {
    private final Map<String, Operation> operations;
    private final Map<String, ClientCard> clientCards;
    private final List<String> numbersOfRecipients;
    private final AtomicLong counter = new AtomicLong(1);
    private int fee;


    public MoneyTransServRepositoryImpl() {
        operations = new ConcurrentHashMap<>();
        clientCards = new ConcurrentHashMap<>();
        numbersOfRecipients = new ArrayList<>();
        cardInit(new ClientCard("2222111133334444", "12/25", "123", 10000, "RUB"));
        cardInit(new ClientCard("1111222233334444", "12/24", "321", 1000, "RUB"));
        cardInit(new ClientCard("1234123412341234", "11/12", "123", 0, "RUB"));
    }

    @Override
    public Operation createTransfer(OperationDtoForTransfer operationDtoForTransfer) {
        if (!(operationDtoForTransfer == null)) {
            Operation newTransfer = new Operation(
                    String.valueOf(counter.getAndIncrement()), clientCards.get(operationDtoForTransfer.getCardFromNumber()),
                    operationDtoForTransfer.getCardToNumber(),
                    operationDtoForTransfer.getAmount(), new Amount(fee, operationDtoForTransfer.getAmount().getCurrency()),
                    OperationStatus.CREATED, new VerificationCode().getCode());
            operations.put(newTransfer.getId(), newTransfer);
            clientCards.put(newTransfer.getCardFromNumber().getNumber(), newTransfer.getCardFromNumber());
            numbersOfRecipients.add(newTransfer.getCardToNumber());
            return newTransfer;
        } else {
            throw new AttemptOfRepeatOperation("It's trying of repeated operation ");
        }
    }

    @Override
    public void cardInit(ClientCard card) {
        clientCards.put(card.getNumber(), card);
        numbersOfRecipients.add(card.getNumber());//TODO delete precipitation, change on clientCards
    }

    @Override
    public Operation getById(String id) {
        return operations.getOrDefault(id, null);
    }

    @Override
    public boolean update(Operation operation) {
        if (operations.containsKey(operation.getId())) {
            operations.put(operation.getId(), operation);
            return true;
        }
        return false;
    }

    @Override
    public boolean confirmClientCardsDB(ClientCard card) {
        return clientCards.containsKey(card.getNumber());
    }

    public void updateClientCard(ClientCard card) {
        if (clientCards.containsKey(card.getNumber())) {
            clientCards.put(card.getNumber(), card);
        }
    }

    @Override
    public ClientCard getCardFromByNumber(String number) {
        return clientCards.get(number);
    }

    @Override
    public String getCardToByNumber(String number) {
        return clientCards.get(number).getNumber();
    }

    @Override
    public ConfirmOperation confirmOperation(Operation operation) {
        VerificationCode code = new VerificationCode();
        return new ConfirmOperation(operation.getId(), code.getCode());
    }

    @Override
    public Map<String, Operation> getByCardSender(ClientCard card) {
        Map<String, Operation> operationsByCard = new HashMap<>();
        for (Operation transfer : operations.values()) {
            if (transfer.getCardFromNumber() == card) {
                operationsByCard.put(transfer.getId(), transfer);
            }
        }
        return operationsByCard;
    }

    @Override
    public Map<String, Operation> getByCardRecipient(String cardOfRecipient) {
        Map<String, Operation> operationsByCard = new HashMap<>();
        for (Operation transfer : operations.values()) {
            if (transfer.getCardToNumber().equals(cardOfRecipient)) {
                operationsByCard.put(transfer.getId(), transfer);
            }
        }
        return operationsByCard;
    }

    @Override
    public Map<String, Operation> getOperationsInIntervalId(String idStart, String idEnd) {
        int start = Integer.parseInt(idStart);
        int end = Integer.parseInt(idEnd);
        Map<String, Operation> operationsInInterval = new HashMap<>();
        if (operations.containsKey(idStart) && operations.containsKey(idEnd) && start < end) {
            for (int i = start; i < end + 1; i++) {
                operationsInInterval.put(String.valueOf(i), operations.get(String.valueOf(i)));
            }
        }
        return operationsInInterval;
    }
}
