package ir.bu.moneytransferservice.repository;

import ir.bu.moneytransferservice.dto.OperationDtoForTransfer;
import ir.bu.moneytransferservice.exception.AttemptOfRepeatOperation;
import ir.bu.moneytransferservice.model.Amount;
import ir.bu.moneytransferservice.model.ClientCard;
import ir.bu.moneytransferservice.model.Operation;
import ir.bu.moneytransferservice.model.OperationStatus;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

public class MoneyTransferServiceRepositoryImplTests {
    MoneyTransferServiceRepository repository = new MoneyTransServRepositoryImpl();
    ClientCard cardFrom = new ClientCard("2222111133334444",
            "12/25",
            "123", 10000, "RUR");
    String cardToNumber = "1111222233334444";
    OperationDtoForTransfer operationDtoForTransfer = new OperationDtoForTransfer(
            cardFrom.getNumber(),
            cardFrom.getValidTill(),
            cardFrom.getCodeCvv(),
            cardToNumber,
            new Amount(100, "RUR"));
    Operation expected = new Operation(
            "1",
            cardFrom, cardToNumber,
            new Amount(100, "RUR"),
            new Amount(1, "RUR"),
            OperationStatus.CREATED,
            "0000");

    @Test
    public void createTransferTest() {
        //arrange
        //act
        Operation actual = repository.createTransfer(operationDtoForTransfer);
        //assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void createTransferThrowingAttemptOfRepeatOperation() {
        //arrange
        repository.getOperations().put(expected.getId(), expected);
        //act
        Executable action = () -> repository.createTransfer(operationDtoForTransfer);
        //assert
        Assertions.assertThrowsExactly(AttemptOfRepeatOperation.class, action, "It's trying of repeat operation ");
    }

    @Test
    public void getOperationByIdTest() {
        //arrange
        repository.getOperations().put(expected.getId(), expected);
        //act
        Operation actual = repository.getOperationById("1").get();
        //assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void updateTest() {
        //arrange
        repository.getOperations().put(expected.getId(), expected);
        //act
        boolean updateOperation = repository.update(expected);
        //assert
        Assertions.assertTrue(updateOperation);
    }

    @Test
    public void getClientCardByNumberTest() {
        //arrange
        repository.getClientCards().put(cardFrom.getNumber(), cardFrom);
        ClientCard expectedCard = new ClientCard("2222111133334444",
                "12/25",
                "123", 1000, "RUR");
        //act
        ClientCard actual = repository.getClientCardByNumber(cardFrom.getNumber());
        //assert
        Assertions.assertEquals(expectedCard, actual);
    }

    @Test
    public void getClientCardByOperationIdTest() {
        //arrange
        ClientCard expectedCardTo = new ClientCard(
                "1111222233334444",
                "12/24", "321", 1000, "RUR");
        repository.getClientCards().put(expectedCardTo.getNumber(), expectedCardTo);
        repository.getOperations().put(expected.getId(), expected);
        //act
        ClientCard actualCardTo = repository.getClientCardByNumber(cardToNumber);
        //assert
        Assertions.assertEquals(expectedCardTo, actualCardTo);
    }
}
