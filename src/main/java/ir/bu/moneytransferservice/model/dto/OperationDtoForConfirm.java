package ir.bu.moneytransferservice.model.dto;

import lombok.Getter;

import java.util.Objects;

@Getter
public class OperationDtoForConfirm {
    private String operationId;
    private String code;

    public OperationDtoForConfirm(String operationId, String code) {
        this.operationId = operationId;
        this.code = code;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public void setCode(String code) {
        this.code = code;
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

    @Override
    public String toString() {
        return "OperationDtoForConfirm{" +
                "operationId='" + operationId + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
