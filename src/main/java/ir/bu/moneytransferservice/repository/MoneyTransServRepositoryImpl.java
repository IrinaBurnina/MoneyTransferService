package ir.bu.moneytransferservice.repository;

import ir.bu.moneytransferservice.dto.OperationDtoForTransfer;
import ir.bu.moneytransferservice.exception.AttemptOfRepeatOperation;
import ir.bu.moneytransferservice.model.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MoneyTransServRepositoryImpl implements MoneyTransferServiceRepository {

    private final Map<String, Operation> operations;
    @Getter
    private final Map<String, ClientCard> clientCards;
    private final AtomicLong counter = new AtomicLong(1);
    public int code;
    @Getter
    @Value("${repository.fee}")
    private int fee = 1;


    public MoneyTransServRepositoryImpl() {
        operations = new ConcurrentHashMap<>();
        clientCards = new ConcurrentHashMap<>();
        cardInit(new ClientCard("2222111133334444", "12/25", "123", 10000, "RUR"));
        cardInit(new ClientCard("1111222233334444", "12/24", "321", 1000, "RUR"));
        cardInit(new ClientCard("1234123412341234", "11/12", "123", 0, "RUB"));
    }

    @Override
    public Operation createTransfer(OperationDtoForTransfer operationDtoForTransfer) {
        Amount amount = operationDtoForTransfer.getAmount();
        Operation newTransfer = new Operation(
                String.valueOf(counter.getAndIncrement()), clientCards.get(operationDtoForTransfer.getCardFromNumber()),
                operationDtoForTransfer.getCardToNumber(),
                amount, calculateFee(amount),
                OperationStatus.CREATED, generateCode());
        if (!operations.containsKey(newTransfer.getId())) {
            operations.put(newTransfer.getId(), newTransfer);
            return newTransfer;
        } else {
            throw new AttemptOfRepeatOperation("It's trying of repeat operation ");
        }
    }

    public String generateCode() {
        code = ThreadLocalRandom.current().nextInt(1000, 10000);
        return String.valueOf(code);
    }

    @Override
    public Amount calculateFee(Amount amount) {
        int valueOfFee = amount.value() * fee / 100;
        return new Amount(valueOfFee, amount.currency());
    }

    @Override
    public void cardInit(ClientCard card) {
        clientCards.put(card.getNumber(), card);
    }

    @Override
    public Optional<Operation> getOperationById(String id) {
        if (operations.containsKey(id)) {
            return Optional.of(operations.get(id));
        }
        return Optional.empty();
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
    public void updateClientCard(ClientCard card) {
        if (clientCards.containsKey(card.getNumber())) {
            clientCards.put(card.getNumber(), card);
        }
    }

    @Override
    public ClientCard getClientCardByNumber(String number) {
        return clientCards.get(number);
    }

    @Override
    public ClientCard getCardFromByIdOperation(String id) {
        Operation o = operations.get(id);
        return o.getCardFromNumber();
    }

    @Override
    public ClientCard getCardToByIdOperation(String id) {
        Operation o = operations.get(id);
        return clientCards.get(o.getCardToNumber());
    }

    @Override
    public Map<String, Operation> getOperations() {
        return operations;
    }

    @Override
    public ConfirmOperation confirmOperation(Operation operation) {
        return new ConfirmOperation(operation.getId(), String.valueOf(code));
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
