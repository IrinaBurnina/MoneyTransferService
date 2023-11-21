package ir.bu.moneytransferservice.model.dto;

import java.util.Objects;

public class OperationDtoResponses {
    private final String id;

    public OperationDtoResponses(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationDtoResponses that = (OperationDtoResponses) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
