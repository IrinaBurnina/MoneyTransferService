package ir.bu.moneytransferservice.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ConfirmOperation {
    private final String operationId;
    private final String verificationCode;

    public ConfirmOperation(String operationId, String verificationCode) {
        this.operationId = operationId;
        this.verificationCode = verificationCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfirmOperation that = (ConfirmOperation) o;
        return Objects.equals(operationId, that.operationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationId);
    }
}
