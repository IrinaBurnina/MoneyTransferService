package ir.bu.moneytransferservice.dto;

public record ErrorDtoResponses(String msg, int id) {
    @Override
    public String msg() {
        return msg;
    }

    @Override
    public int id() {
        return id;
    }
}
