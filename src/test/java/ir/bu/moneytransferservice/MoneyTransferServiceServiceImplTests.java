package ir.bu.moneytransferservice;

import ir.bu.moneytransferservice.exception.ExpenseOverBalance;
import ir.bu.moneytransferservice.exception.InvalidInputData;
import ir.bu.moneytransferservice.exception.NonConfirmOperation;
import ir.bu.moneytransferservice.exception.NotFoundException;
import ir.bu.moneytransferservice.logger.Log;
import ir.bu.moneytransferservice.model.Amount;
import ir.bu.moneytransferservice.model.ClientCard;
import ir.bu.moneytransferservice.model.Operation;
import ir.bu.moneytransferservice.model.OperationStatus;
import ir.bu.moneytransferservice.model.dto.OperationDtoForConfirm;
import ir.bu.moneytransferservice.model.dto.OperationDtoForTransfer;
import ir.bu.moneytransferservice.model.dto.OperationDtoResponses;
import ir.bu.moneytransferservice.repository.MoneyTransferServiceRepository;
import ir.bu.moneytransferservice.service.MoneyTransServiceServiceImpl;
import ir.bu.moneytransferservice.service.MoneyTransferServiceService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.util.Optional;


public class MoneyTransferServiceServiceImplTests {

    String cardToNumber = "1111222233334444";
    MoneyTransferServiceRepository repository = Mockito.mock(MoneyTransferServiceRepository.class);
    Log logger = Mockito.mock(Log.class);

    MoneyTransferServiceService service;

    ClientCard cardFrom = new ClientCard(
            "2222111133334444",
            "12/25",
            "123",
            1000, "RUB");
    ClientCard cardTo = new ClientCard(
            cardToNumber,
            "12/24",
            "321",
            1000,
            "RUR");
    Operation operation = new Operation("1", cardFrom,
            cardToNumber, new Amount("RUR", 100),
            new Amount("RUR", 1), OperationStatus.SUCCESS_TRANSFER, "0000");


    @Test
    public void testTransfer() {
        //arrange
        Mockito.when(repository.getClientCardByNumber(cardFrom.getNumber())).thenReturn(cardFrom);
        OperationDtoForTransfer operationDtoForTransfer
                = new OperationDtoForTransfer(cardFrom.getNumber(),
                cardFrom.getValidTill(),
                cardFrom.getCodeCvv(),
                cardToNumber, new Amount("RUR", 400));
        Mockito.when(repository.createTransfer(operationDtoForTransfer)).thenReturn(operation);
        service = new MoneyTransServiceServiceImpl(repository, logger);
        OperationDtoResponses expected = new OperationDtoResponses("1");
        //act
        OperationDtoResponses actual = service.transfer(operationDtoForTransfer);
        //assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testTransferThrowingInvalidInputData() {
        //arrange
        Mockito.when(repository.getClientCardByNumber(cardFrom.getNumber())).thenReturn(cardFrom);
        OperationDtoForTransfer operationDtoForTransfer
                = new OperationDtoForTransfer(cardFrom.getNumber(),
                cardFrom.getValidTill(),
                "000",
                cardToNumber, new Amount("RUR", 400));
        Mockito.when(repository.createTransfer(operationDtoForTransfer)).thenReturn(operation);
        service = new MoneyTransServiceServiceImpl(repository, logger);
        //act
        Executable action = () -> service.transfer(operationDtoForTransfer);
        //assert
        Assertions.assertThrowsExactly(InvalidInputData.class, action, "Unknown input data (card number or date valid till or code CVV)");
    }

    @Test
    public void testTransferThrowingNotFound() {
        //arrange
        Mockito.when(repository.getClientCardByNumber(cardFrom.getNumber())).thenReturn(null);
        OperationDtoForTransfer operationDtoForTransfer
                = new OperationDtoForTransfer(cardFrom.getNumber(),
                cardFrom.getValidTill(),
                cardFrom.getCodeCvv(),
                cardToNumber, new Amount("RUR", 400));
        Mockito.when(repository.createTransfer(operationDtoForTransfer)).thenReturn(operation);
        service = new MoneyTransServiceServiceImpl(repository, logger);
        //act
        Executable action = () -> service.transfer(operationDtoForTransfer);
        //assert
        Assertions.assertThrowsExactly(NotFoundException.class, action, "Card FROM is not found");
    }

    @Test
    public void testTransferThrowingExpenseOverBalance() {
        //arrange
        Mockito.when(repository.getClientCardByNumber(cardFrom.getNumber())).thenReturn(cardFrom);
        OperationDtoForTransfer operationDtoForTransfer
                = new OperationDtoForTransfer(cardFrom.getNumber(),
                cardFrom.getValidTill(),
                cardFrom.getCodeCvv(),
                cardToNumber, new Amount("RUR", 4000000));
        Mockito.when(repository.createTransfer(operationDtoForTransfer)).thenReturn(operation);
        service = new MoneyTransServiceServiceImpl(repository, logger);
        //act
        Executable action = () -> service.transfer(operationDtoForTransfer);
        //assert
        Assertions.assertThrowsExactly(ExpenseOverBalance.class, action, "Not enough money");
    }

    @Test
    public void testConfirmOperation() {
        //arrange
        OperationDtoForConfirm operationDtoForConfirm = new OperationDtoForConfirm("1", "0000");
        Mockito.when(repository.getOperationById(operationDtoForConfirm.getOperationId())).thenReturn(Optional.ofNullable(operation));
        Mockito.when(repository.getCardFromByIdOperation(operationDtoForConfirm.getOperationId())).thenReturn(cardFrom);
        Mockito.when(repository.getCardToByIdOperation(operationDtoForConfirm.getOperationId())).thenReturn(cardTo);
        Mockito.when(repository.getClientCardByNumber(operation.getCardToNumber())).thenReturn(cardTo);
        service = new MoneyTransServiceServiceImpl(repository, logger);
        OperationDtoResponses expectedOperationDtoResponses = new OperationDtoResponses("1");
        //act
        OperationDtoResponses actualOperationDtoResponses = service.confirm(operationDtoForConfirm);
        //arrange
        Assertions.assertEquals(expectedOperationDtoResponses, actualOperationDtoResponses);
    }

    @Test
    public void testConfirmOperationThrowingNotFound() {
        //arrange
        OperationDtoForConfirm operationDtoForConfirm = new OperationDtoForConfirm("1", "0000");
        Mockito.when(repository.getOperationById(operationDtoForConfirm.getOperationId())).thenReturn(Optional.ofNullable(null));
        service = new MoneyTransServiceServiceImpl(repository, logger);
        //act
        Executable action = () -> service.confirm(operationDtoForConfirm);
        //assert
        Assertions.assertThrowsExactly(NotFoundException.class, action, "Operation isn't created");
    }

    @Test
    public void testConfirmOperationThrowingNonConfirmOperation() {
        //arrange
        OperationDtoForConfirm operationDtoForConfirm = new OperationDtoForConfirm("1", "0001");
        Mockito.when(repository.getOperationById(operationDtoForConfirm.getOperationId())).thenReturn(Optional.ofNullable(operation));
        service = new MoneyTransServiceServiceImpl(repository, logger);
        //act
        Executable action = () -> service.confirm(operationDtoForConfirm);
        //assert
        Assertions.assertThrowsExactly(NonConfirmOperation.class, action, "Operation isn't confirming");
    }

}
