package ir.bu.moneytransferservice.service;

import ir.bu.moneytransferservice.dto.OperationDtoForConfirm;
import ir.bu.moneytransferservice.dto.OperationDtoForTransfer;
import ir.bu.moneytransferservice.dto.OperationDtoResponses;

public interface MoneyTransferServiceService {
    OperationDtoResponses transfer(OperationDtoForTransfer operationDtoForTransfer);

    OperationDtoResponses confirm(OperationDtoForConfirm operationDtoForConfirm);
}
