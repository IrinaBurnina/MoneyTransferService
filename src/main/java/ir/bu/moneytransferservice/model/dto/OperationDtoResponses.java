package ir.bu.moneytransferservice.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class OperationDtoResponses {
    private String operationId;

    public OperationDtoResponses(String operationId) {
        this.operationId = operationId;
    }

    public OperationDtoResponses() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationDtoResponses that = (OperationDtoResponses) o;
        return Objects.equals(operationId, that.operationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationId);
    }

    @Override
    public String toString() {
        return "OperationDtoResponses{" +
                "operationId='" + operationId + '\'' +
                '}';
    }
}
