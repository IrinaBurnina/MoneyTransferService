package ir.bu.moneytransferservice.exception;

public class OperationError extends RuntimeException {
    public OperationError(String msg) {
        super(msg);
    }
}
