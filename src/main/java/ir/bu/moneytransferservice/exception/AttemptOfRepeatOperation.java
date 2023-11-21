package ir.bu.moneytransferservice.exception;

public class AttemptOfRepeatOperation extends RuntimeException {
    public AttemptOfRepeatOperation(String msg) {
        super(msg);
    }
}
