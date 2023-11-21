package ir.bu.moneytransferservice.exception;

public class ExpenseOverBalance extends RuntimeException {
    public ExpenseOverBalance(String msg) {
        super(msg);
    }
}
