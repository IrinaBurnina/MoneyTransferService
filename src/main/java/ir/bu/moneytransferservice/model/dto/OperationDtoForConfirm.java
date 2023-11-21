package ir.bu.moneytransferservice.model.dto;

import lombok.Getter;

import java.util.Objects;

public class OperationDtoForConfirm {
    @Getter
    private String operationId;
    @Getter
    private String verificationCode;

    public OperationDtoForConfirm(String operationId, String verificationCode) {
        this.operationId = operationId;
        this.verificationCode = verificationCode;
    }

    public OperationDtoForConfirm() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationDtoForConfirm that = (OperationDtoForConfirm) o;
        return Objects.equals(operationId, that.operationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationId);
    }
}
