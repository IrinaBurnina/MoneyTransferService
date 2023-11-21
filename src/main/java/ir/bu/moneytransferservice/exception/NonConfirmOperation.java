package ir.bu.moneytransferservice.exception;

public class NonConfirmOperation extends RuntimeException {
    public NonConfirmOperation(String msg) {
        super(msg);
    }
}
