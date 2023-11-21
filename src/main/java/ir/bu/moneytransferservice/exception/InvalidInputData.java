package ir.bu.moneytransferservice.exception;

public class InvalidInputData extends RuntimeException {
    public InvalidInputData(String msg) {
        super(msg);
    }
}
