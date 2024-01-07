package ir.bu.moneytransferservice.advice;

import ir.bu.moneytransferservice.dto.ErrorDtoResponses;
import ir.bu.moneytransferservice.exception.*;
import ir.bu.moneytransferservice.exception.errors.ErrorsForMoneyTransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(ExpenseOverBalance.class)
    public ResponseEntity<ErrorDtoResponses> expenseOverBalanceHandler(ExpenseOverBalance e) {
        return new ResponseEntity<>(new ErrorDtoResponses(e.getMessage(),
                ErrorsForMoneyTransferService.EXPENSE_OVER_BALANCE.getCode()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidInputData.class)
    public ResponseEntity<ErrorDtoResponses> invalidInputDataHandler(InvalidInputData e) {
        return new ResponseEntity<>(new ErrorDtoResponses(e.getMessage(),
                ErrorsForMoneyTransferService.INVALID_INPUT_DATA.getCode()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NonConfirmOperation.class)
    public ResponseEntity<ErrorDtoResponses> nonConfirmOperationHandler(NonConfirmOperation e) {
        return new ResponseEntity<>(new ErrorDtoResponses(e.getMessage(),
                ErrorsForMoneyTransferService.NON_CONFIRM_OPERATION.getCode()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDtoResponses> notFoundExceptionHandler(NotFoundException e) {
        return new ResponseEntity<>(new ErrorDtoResponses(e.getMessage(),
                ErrorsForMoneyTransferService.NOT_FOUND_EXCEPTION.getCode()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OperationError.class)
    public ResponseEntity<ErrorDtoResponses> operationErrorHandler(OperationError e) {
        return new ResponseEntity<>(new ErrorDtoResponses(e.getMessage(),
                ErrorsForMoneyTransferService.OPERATION_ERROR.getCode()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AttemptOfRepeatOperation.class)
    public ResponseEntity<ErrorDtoResponses> attemptOfRepeatOperationHandler(AttemptOfRepeatOperation e) {
        return new ResponseEntity<>(new ErrorDtoResponses(e.getMessage(),
                ErrorsForMoneyTransferService.ATTEMPT_OF_REPEATED_OPERATION.getCode()), HttpStatus.BAD_REQUEST);
    }
}
