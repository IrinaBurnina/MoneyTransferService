package ir.bu.moneytransferservice.exception;

import lombok.Getter;

@Getter
public enum ErrorsForMoneyTransferService {
    EXPENSE_OVER_BALANCE(0, "Not enough money."),
    INVALID_INPUT_DATA(1, "The entered data isn't corrected."),
    NON_CONFIRM_OPERATION(2, "The operation has not been confirmed."),
    NOT_FOUND_EXCEPTION(3, "Data isn't found."),
    OPERATION_ERROR(4, "Error in operation is occurred."),
    ATTEMPT_OF_REPEATED_OPERATION(5, "Attempt to perform the operation again.");

    private final int code;
    private final String description;

    private ErrorsForMoneyTransferService(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }

}
