package ir.bu.moneytransferservice.controller;

import ir.bu.moneytransferservice.dto.OperationDtoForConfirm;
import ir.bu.moneytransferservice.dto.OperationDtoForTransfer;
import ir.bu.moneytransferservice.dto.OperationDtoResponses;
import org.springframework.http.ResponseEntity;

public interface MoneyTransferServiceController {
    ResponseEntity<OperationDtoResponses> transfer(OperationDtoForTransfer operationDtoForTransfer);

    ResponseEntity<OperationDtoResponses> confirm(OperationDtoForConfirm operationDtoForConfirm);

}
