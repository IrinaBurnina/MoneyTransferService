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
