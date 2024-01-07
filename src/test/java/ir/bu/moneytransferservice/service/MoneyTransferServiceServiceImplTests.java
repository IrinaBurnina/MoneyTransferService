package ir.bu.moneytransferservice.service;

import ir.bu.moneytransferservice.dto.OperationDtoForConfirm;
import ir.bu.moneytransferservice.dto.OperationDtoForTransfer;
import ir.bu.moneytransferservice.dto.OperationDtoResponses;
import ir.bu.moneytransferservice.logger.Log;
import ir.bu.moneytransferservice.model.Amount;
import ir.bu.moneytransferservice.model.ClientCard;
import ir.bu.moneytransferservice.model.Operation;
import ir.bu.moneytransferservice.model.OperationStatus;
import ir.bu.moneytransferservice.repository.MoneyTransferServiceRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;


public class MoneyTransferServiceServiceImplTests {

    String cardToNumber = "1111222233334444";
    MoneyTransferServiceRepository repository = Mockito.mock(MoneyTransferServiceRepository.class);
    Log logger = Mockito.mock(Log.class);
    ThrowingExceptionsService exceptionsServ = Mockito.mock(ThrowingExceptionsService.class);
    MoneyTransServiceServiceImpl service = new MoneyTransServiceServiceImpl(repository, logger, exceptionsServ);
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
            cardToNumber, new Amount(100, "RUR"),
            new Amount(10, "RUR"), OperationStatus.SUCCESS_TRANSFER, "0000");


    @Test
    public void transferTest() {
        //arrange
        OperationDtoForTransfer operationDtoForTransfer
                = new OperationDtoForTransfer(cardFrom.getNumber(),
                cardFrom.getValidTill(),
                cardFrom.getCodeCvv(),
                cardToNumber, new Amount(400, "RUR"));
        OperationDtoResponses expected =
                new OperationDtoResponses("1");
        Mockito.when(service.checksOfTransfer(operationDtoForTransfer)).thenReturn(true);
        Mockito.when(repository.createTransfer(operationDtoForTransfer)).thenReturn(operation);
        //act
        OperationDtoResponses actual = service.transfer(operationDtoForTransfer);
        //assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void checksOfTransferTest() {
        //arrange
        Amount amount = new Amount(100, "RUR");
        OperationDtoForTransfer operationDtoForTransfer = new OperationDtoForTransfer(
                cardFrom.getNumber(),
                cardFrom.getValidTill(),
                cardFrom.getCodeCvv(),
                cardToNumber,
                amount);
        Mockito.when(exceptionsServ.checkEmptyFields(operationDtoForTransfer, logger)).thenReturn(true);
        Mockito.when(exceptionsServ.checkMinimalAmount(amount, logger)).thenReturn(true);
        Mockito.when(exceptionsServ.checkClientCard(cardFrom, logger, "FROM")).thenReturn(true);
        Mockito.when(exceptionsServ.checkIncorrectData(cardFrom, operationDtoForTransfer, logger)).thenReturn(true);
        Mockito.when(exceptionsServ.checkClientCard(cardTo, logger, "TO")).thenReturn(true);
        Mockito.when(exceptionsServ.checkEnoughMoney(cardFrom, operationDtoForTransfer, logger)).thenReturn(true);
        MoneyTransServiceServiceImpl serviceImpl = new MoneyTransServiceServiceImpl(repository, logger, exceptionsServ);
        //act
        boolean actual = serviceImpl.checksOfTransfer(operationDtoForTransfer);
        //assert
        Assertions.assertTrue(actual);
    }

    @Test
    public void withSetVerificationCodeTest() {
        //arrange
        int wantedNumberOfInvocations = 1;
        Operation operationMock = Mockito.mock(Operation.class);
        //act
        service.withSetVerificationCode(operationMock);
        //assert
        Mockito.verify(operationMock, Mockito.times(wantedNumberOfInvocations)).setVerificationCode("0000");
    }

    @Test
    public void writeNewOperationSettingsTest() {
        //arrange
        String id = "1";
        OperationDtoResponses expected = new OperationDtoResponses(id);
        Mockito.when(repository.getCardFromByIdOperation(id)).thenReturn(cardFrom);
        Mockito.when(repository.getCardToByIdOperation(id)).thenReturn(cardTo);
        Mockito.when(repository.getClientCardByNumber(operation.getCardToNumber())).thenReturn(cardTo);
        Mockito.when(repository.calculateFee(operation.getAmount())).thenReturn(operation.getFee());
        service = new MoneyTransServiceServiceImpl(repository, logger, exceptionsServ);
        //act
        OperationDtoResponses actual = service.writeNewOperationSettings(operation);
        //assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void confirmTest() {
        //arrange
        OperationDtoForConfirm operationDtoForConfirm = new OperationDtoForConfirm("1", "0000");
        String id = operationDtoForConfirm.operationId();
        Mockito.when(repository.getCardFromByIdOperation(id)).thenReturn(cardFrom);
        Mockito.when(repository.getCardToByIdOperation(id)).thenReturn(cardTo);
        Mockito.when(repository.getClientCardByNumber(operation.getCardToNumber())).thenReturn(cardTo);
        Mockito.when(repository.calculateFee(operation.getAmount())).thenReturn(operation.getFee());
        OperationDtoResponses expectedOperationDtoResponses = new OperationDtoResponses("1");

        Mockito.when(exceptionsServ.checkNotFoundOperation(id, logger, repository))
                .thenReturn(operation);
        Mockito.when(exceptionsServ.checkConfirmCode(operationDtoForConfirm, operation, repository, logger))
                .thenReturn(true);
        //act
        OperationDtoResponses actualOperationDtoResponses = service.confirm(operationDtoForConfirm);
        //arrange
        Assertions.assertEquals(expectedOperationDtoResponses, actualOperationDtoResponses);
    }

    @Test
    public void calculateOfFeeTest() {
        //arrange
        Amount expected = operation.getFee();
        Mockito.when(repository.calculateFee(operation.getAmount())).thenReturn(operation.getFee());
        //  service = new MoneyTransServiceServiceImpl(repository, logger, exceptionsServ);
        //act
        Amount actual = service.calculateOfFee(operation);
        //assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getCardByNumberTest() {
        //arrange
        Mockito.when(repository.getClientCardByNumber(cardFrom.getNumber()))
                .thenReturn(cardFrom);
        //act
        ClientCard actual = service.getCardByNumber("2222111133334444");
        //assert
        Assertions.assertEquals(cardFrom, actual);
    }

    @Test
    public void getCardFromById() {
        //arrange
        Mockito.when(repository.getCardFromByIdOperation(operation.getId())).thenReturn(cardFrom);
        //act
        ClientCard actual = service.getCardFromById(operation.getId());
        //assert
        Assertions.assertEquals(cardFrom, actual);
    }

    @Test
    public void getCardToById() {
        //arrange
        Mockito.when(repository.getCardToByIdOperation(operation.getId())).thenReturn(cardTo);
        //act
        ClientCard actual = service.getCardToById(operation.getId());
        //assert
        Assertions.assertEquals(cardTo, actual);
    }

    @Test
    public void calculateNewBalanceCardFromTest() {
        //arrange
        int sum = operation.getAmount().value();
        int fee = operation.getFee().value();
        int expected = cardFrom.getBalance() - sum - fee;
        Mockito.when(repository.calculateFee(operation.getAmount())).thenReturn(operation.getFee());
        //act
        int actual = service.calculateNewBalanceCardFrom(operation);
        //assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void calculateAmountOfTransfer() {
        //arrange
        int sum = operation.getAmount().value();
        int fee = operation.getFee().value();
        int expected = sum + fee;
        Mockito.when(repository.calculateFee(operation.getAmount())).thenReturn(operation.getFee());
        //act
        int actual = service.calculateAmountOfTransfer(operation);
        //assert
        Assertions.assertEquals(expected, actual);
    }

}
