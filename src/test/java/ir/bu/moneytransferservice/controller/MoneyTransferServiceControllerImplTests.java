package ir.bu.moneytransferservice.controller;

import ir.bu.moneytransferservice.dto.OperationDtoForConfirm;
import ir.bu.moneytransferservice.dto.OperationDtoForTransfer;
import ir.bu.moneytransferservice.dto.OperationDtoResponses;
import ir.bu.moneytransferservice.model.Amount;
import ir.bu.moneytransferservice.service.MoneyTransferServiceService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MoneyTransferServiceControllerImplTests {
    MoneyTransferServiceService service = Mockito.mock(MoneyTransferServiceService.class);
    MoneyTransferServiceController controller = new MoneyTransferServiceControllerImpl(service);
    OperationDtoForTransfer operationDtoForTransfer = new OperationDtoForTransfer(
            "2222111133334444",
            "12/25",
            "123",
            "1111222233334444",
            new Amount(100, "RUR")
    );
    OperationDtoForConfirm operationDtoForConfirm = new OperationDtoForConfirm("1", "0000");
    OperationDtoResponses expected = new OperationDtoResponses("1");
    ResponseEntity<OperationDtoResponses> expectedEntity = new ResponseEntity<>(expected, HttpStatus.OK);

    @Test
    public void transferTest() {
        //arrange
        Mockito.when(service.transfer(operationDtoForTransfer)).thenReturn(expected);
        //act
        ResponseEntity<OperationDtoResponses> actual = controller.transfer(operationDtoForTransfer);
        //assert
        Assertions.assertEquals(expectedEntity, actual);
    }

    @Test
    public void confirmOperation() {
        //arrange
        Mockito.when(service.confirm(operationDtoForConfirm)).thenReturn(expected);
        //act
        ResponseEntity<OperationDtoResponses> actual = controller.confirm(operationDtoForConfirm);
        //assert
        Assertions.assertEquals(expectedEntity, actual);
    }

}
