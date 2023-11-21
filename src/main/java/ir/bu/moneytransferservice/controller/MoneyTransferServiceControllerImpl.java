package ir.bu.moneytransferservice.controller;

import ir.bu.moneytransferservice.model.dto.OperationDtoForConfirm;
import ir.bu.moneytransferservice.model.dto.OperationDtoForTransfer;
import ir.bu.moneytransferservice.model.dto.OperationDtoResponses;
import ir.bu.moneytransferservice.service.MoneyTransServiceServiceImpl;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://serp-ya.github.io", maxAge = 3600)
@RestController
@RequestMapping("")
public class MoneyTransferServiceControllerImpl implements MoneyTransferServiceController {
    private final MoneyTransServiceServiceImpl service;

    public MoneyTransferServiceControllerImpl(MoneyTransServiceServiceImpl service) {
        this.service = service;
    }

    @PostMapping("/transfer")
    @Override
    public OperationDtoResponses transfer(@RequestBody OperationDtoForTransfer operationDtoForTransfer) {
        return service.transfer(operationDtoForTransfer);
    }

    @PostMapping("/confirmOperation")
    @Override
    public OperationDtoResponses confirm(@RequestBody OperationDtoForConfirm operationDtoForConfirm) {
        return service.confirm(operationDtoForConfirm);
    }

    @GetMapping("/ping")
    public String ping() {
        return "OK";
    }
}
