package ir.bu.moneytransferservice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class Operation {
    @Getter
    private final String id;
    @Getter
    private final ClientCard cardFromNumber;
    @Getter
    private final String cardToNumber;
    @Getter
    private final Amount amount;
    @Getter
    @Setter
    private Amount fee;
    @Getter
    @Setter
    private OperationStatus operationStatus;
    @Getter
    @Setter
    private String verificationCode;

    public Operation(String id, ClientCard cardFromNumber, String cardToNumber, Amount amount, Amount fee,
                     OperationStatus operationStatus, String verificationCode) {
        this.id = id;
        this.cardFromNumber = cardFromNumber;
        this.cardToNumber = cardToNumber;
        this.amount = amount;
        this.fee = fee;
        this.operationStatus = operationStatus;
        this.verificationCode = verificationCode;
    }

//    public List<Operation> all() {//TODO сделать вывод операций по интервалу времени или по ид
//        return new ArrayList<>(operations.values());
//    }
//
//    public Optional<Operation> getById(String id) {
//        if (operations.containsKey(id)) {
//            return Optional.ofNullable(operations.get(id));
//        }
//        System.out.println("Запрашиваемой операции не существует");//TODO выбросить ошибку not found
//        return Optional.empty();
//    }
//
//    public void save(Operation operation) {
//        if (operation.getId().equals(0)) {
//            Long id =counter.getAndIncrement();
//            Operation operation1 = new Operation(String.valueOf(id),
//                    new ClientCard(),
//                    new ClientCard(),
//                    operation.getAmount(), operation.getFee(), operationStatus, operations);
//            operations.put(id, operation1);
//           //TODO сделать ответ ОК 200, а также снятие и поступление денег на счет?
//        } else if (!operation.getId().equals(0) && operations.containsKey(operation.getId())) {
//
//
//        }
//        System.out.println("Невозможно выполнить обновление поста. Пост с id = " + operation.getId() + " был удален или не существует.");
//        return null;
//    }


    @Override
    public String toString() {
        return "Operation{" +
                "OperationId='" + id + '\'' +
                ", sender=" + cardFromNumber +
                ", recipient=" + cardToNumber +
                ", amount=" + amount +
                ", fee=" + fee +
                ", status=" + operationStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return Objects.equals(id, operation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
