package ir.bu.moneytransferservice;

import ir.bu.moneytransferservice.controller.MoneyTransferServiceController;
import ir.bu.moneytransferservice.controller.MoneyTransferServiceControllerImpl;
import ir.bu.moneytransferservice.model.Amount;
import ir.bu.moneytransferservice.model.dto.OperationDtoForConfirm;
import ir.bu.moneytransferservice.model.dto.OperationDtoForTransfer;
import ir.bu.moneytransferservice.model.dto.OperationDtoResponses;
import ir.bu.moneytransferservice.service.MoneyTransferServiceService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

public class MoneyTransferServiceControllerImplTests {
    MoneyTransferServiceService service = Mockito.mock(MoneyTransferServiceService.class);
    MoneyTransferServiceController controller = new MoneyTransferServiceControllerImpl(service);
    OperationDtoForTransfer operationDtoForTransfer = new OperationDtoForTransfer(
            "2222111133334444",
            "12/25",
            "123",
            "1111222233334444",
            new Amount("RUR", 100)
    );
    OperationDtoForConfirm operationDtoForConfirm = new OperationDtoForConfirm("1", "0000");
    OperationDtoResponses expected = new OperationDtoResponses("1");

    @Test
    public void transferTest() {
        //arrange
        Mockito.when(service.transfer(operationDtoForTransfer)).thenReturn(expected);
        //act
        OperationDtoResponses actual = controller.transfer(operationDtoForTransfer);
        //assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void confirmOperation() {
        //arrange
        Mockito.when(service.confirm(operationDtoForConfirm)).thenReturn(expected);
        //act
        OperationDtoResponses actual = controller.confirm(operationDtoForConfirm);
        //assert
        Assertions.assertEquals(expected, actual);
    }

}
