package ir.bu.moneytransferservice.controller;

import ir.bu.moneytransferservice.model.dto.OperationDtoForConfirm;
import ir.bu.moneytransferservice.model.dto.OperationDtoForTransfer;
import ir.bu.moneytransferservice.model.dto.OperationDtoResponses;

public interface MoneyTransferServiceController {
    OperationDtoResponses transfer(OperationDtoForTransfer operationDtoForTransfer);

    OperationDtoResponses confirm(OperationDtoForConfirm operationDtoForConfirm);

}
