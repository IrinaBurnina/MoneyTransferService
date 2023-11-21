package ir.bu.moneytransferservice.model;

public class ConfirmOperation {
    private String operationId;
    private String verificationCode;

    public ConfirmOperation(String operationId, String verificationCode) {
        this.operationId = operationId;
        this.verificationCode = verificationCode;
    }
}
