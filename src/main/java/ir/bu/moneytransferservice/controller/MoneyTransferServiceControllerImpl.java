package ir.bu.moneytransferservice.controller;

import ir.bu.moneytransferservice.dto.OperationDtoForConfirm;
import ir.bu.moneytransferservice.dto.OperationDtoForTransfer;
import ir.bu.moneytransferservice.dto.OperationDtoResponses;
import ir.bu.moneytransferservice.service.MoneyTransferServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "${settings.cross_origin}", maxAge = 3600)
//"#{T(java.lang.Long).parse('${${settings.max_age}}')}"
@RestController
@RequiredArgsConstructor
@RequestMapping("/transfer")
public class MoneyTransferServiceControllerImpl implements MoneyTransferServiceController {
    private final MoneyTransferServiceService service;

    @PostMapping("/transfer")
    @Override
    public ResponseEntity<OperationDtoResponses> transfer(@RequestBody OperationDtoForTransfer operationDtoForTransfer) {
        return new ResponseEntity<>(service.transfer(operationDtoForTransfer), HttpStatus.OK);
    }

    @PostMapping("/confirmOperation")
    @Override
    public ResponseEntity<OperationDtoResponses> confirm(@RequestBody OperationDtoForConfirm operationDtoForConfirm) {
        return new ResponseEntity<>(service.confirm(operationDtoForConfirm), HttpStatus.OK);
    }
}
