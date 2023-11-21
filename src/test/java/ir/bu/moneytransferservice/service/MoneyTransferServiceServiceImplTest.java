package ir.bu.moneytransferservice.service;

import ir.bu.moneytransferservice.logger.Log;
import ir.bu.moneytransferservice.model.Amount;
import ir.bu.moneytransferservice.model.ClientCard;
import ir.bu.moneytransferservice.model.Operation;
import ir.bu.moneytransferservice.model.OperationStatus;
import ir.bu.moneytransferservice.model.dto.OperationDtoForTransfer;
import ir.bu.moneytransferservice.model.dto.OperationDtoResponses;
import ir.bu.moneytransferservice.repository.MoneyTransServRepositoryImpl;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;


public class MoneyTransferServiceServiceImplTest {

    String cardTo = "1111222233334444";
    MoneyTransServRepositoryImpl repository;
    Log logger;
    OperationDtoForTransfer operationDtoForTransfer;
    MoneyTransServiceServiceImpl service;

    ClientCard cardFrom = new ClientCard(
            "2222111133334444",
            "12/25",
            "123",
            1000, "RUB");
    OperationDtoResponses dtoResponsesExpected;

    //@ParameterizedTest
//@MethodSource("OperationDtoForTransferComponents")
    @Test
    public void transfer() {
        //arrange
        repository = Mockito.mock(MoneyTransServRepositoryImpl.class);
        logger = Mockito.mock(Log.class);
        service = Mockito.mock(MoneyTransServiceServiceImpl.class);
        Operation operation = new Operation("1", cardFrom,
                cardTo, new Amount(100, "RUB"),
                new Amount(10, "RUB"), OperationStatus.SUCCESS_TRANSFER, "0000");
        Operation operationNullId = new Operation(null, cardFrom,
                cardTo, new Amount(100, "RUB"),
                new Amount(10, "RUB"), OperationStatus.SUCCESS_TRANSFER, "0000");
//
        operationDtoForTransfer
                = new OperationDtoForTransfer("2222111133334444",
                "12/25",
                "123",
                cardTo, new Amount(100, "RUB"));

        Mockito.when(repository.getCardFromByNumber("2222111133334444")).thenReturn(cardFrom);

        Mockito.when(repository.getCardFromByNumber(operationDtoForTransfer.getCardFromNumber())).thenReturn(cardFrom);
        Mockito.when(repository.getCardToByNumber(operationDtoForTransfer.getCardToNumber())).thenReturn(cardTo);
        Mockito.when(repository.createTransfer(operationDtoForTransfer)).thenReturn(operation);

        ClientCard cardFrom7 = repository.getCardFromByNumber("2222111133334444");
        // System.out.println(cardFrom+"    "+repository.getCardByNumber("2222111133334444"));
        // OperationDtoForTransfer operationWithoutId=new OperationDtoForTransfer();


        // Mockito.when(repository.createTransfer(operationDtoForTransfer)).thenReturn(operation);
        System.out.println(operation + "\n" + operationDtoForTransfer);

        service = new MoneyTransServiceServiceImpl(repository, logger);

//        Mockito.when(service.withSetVerificationCode(operationNullId)).thenReturn(operation);

        OperationDtoResponses expected = new OperationDtoResponses("1");
        System.out.println(service);
        //act
        OperationDtoResponses actual = service.transfer(operationDtoForTransfer);
        //assert
        Assertions.assertEquals(expected, actual);
    }

    @BeforeEach
    void setUp() {
//        repository = Mockito.mock(MoneyTransferServiceRepository.class);
//        logger = Mockito.mock(Log.class);

    }
}
