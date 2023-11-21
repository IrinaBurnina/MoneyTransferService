package ir.bu.moneytransferservice.service;

import ir.bu.moneytransferservice.model.dto.OperationDtoForConfirm;
import ir.bu.moneytransferservice.model.dto.OperationDtoForTransfer;
import ir.bu.moneytransferservice.model.dto.OperationDtoResponses;

public interface MoneyTransferServiceService {
    OperationDtoResponses transfer(OperationDtoForTransfer operationDtoForTransfer);

    OperationDtoResponses confirm(OperationDtoForConfirm operationDtoForConfirm);
}
