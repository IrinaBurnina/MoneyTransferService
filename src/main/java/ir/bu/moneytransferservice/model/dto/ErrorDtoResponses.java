package ir.bu.moneytransferservice.model.dto;

import lombok.Getter;

@Getter
public class ErrorDtoResponses {
    private final String msg;
    private final int id;

    public ErrorDtoResponses(String msg, int id) {
        this.msg = msg;
        this.id = id;
    }

    @Override
    public String toString() {
        return " ErrorDtoResponses{" +
                " msg= '" + msg + '\'' +
                ", id= " + id +
                '}';
    }
}
