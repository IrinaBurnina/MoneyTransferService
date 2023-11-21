package ir.bu.moneytransferservice.model.dto;

public class ErrorDtoResponses {
    private final String msg;
    private final int id;

    public ErrorDtoResponses(String msg, int id) {
        this.msg = msg;
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ErrorDtoResponses{" +
                "msg='" + msg + '\'' +
                ", id=" + id +
                '}';
    }
}
