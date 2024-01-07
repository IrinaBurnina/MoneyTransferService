package ir.bu.moneytransferservice.service;

import ir.bu.moneytransferservice.dto.OperationDtoForConfirm;
import ir.bu.moneytransferservice.dto.OperationDtoForTransfer;
import ir.bu.moneytransferservice.exception.ExpenseOverBalance;
import ir.bu.moneytransferservice.exception.InvalidInputData;
import ir.bu.moneytransferservice.exception.NonConfirmOperation;
import ir.bu.moneytransferservice.exception.NotFoundException;
import ir.bu.moneytransferservice.logger.Log;
import ir.bu.moneytransferservice.model.Amount;
import ir.bu.moneytransferservice.model.ClientCard;
import ir.bu.moneytransferservice.model.Operation;
import ir.bu.moneytransferservice.model.OperationStatus;
import ir.bu.moneytransferservice.repository.MoneyTransferServiceRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.stream.Stream;

public class ThrowingExceptionServiceTests {
    String cardToNumber = "1111222233334444";
    ClientCard cardFrom = new ClientCard(
            "2222111133334444",
            "12/25",
            "123",
            1000, "RUR");
    Log logger = Mockito.mock(Log.class);
    MoneyTransferServiceRepository repository = Mockito.mock(MoneyTransferServiceRepository.class);
    ThrowingExceptionsService exceptionsServ = new ThrowingExceptionsService();


    @ParameterizedTest
    @MethodSource("sourceMethodForCheckInvalidInputDataTest")
    public void checkIncorrectDataTest(ClientCard cardFromTest) {
        //arrange
        OperationDtoForTransfer operationDtoForTransfer
                = new OperationDtoForTransfer(cardFrom.getNumber(),
                cardFrom.getValidTill(),
                cardFrom.getCodeCvv(),
                cardToNumber, new Amount(400, "RUR"));
        //act
        Executable action = () -> exceptionsServ
                .checkIncorrectData(cardFromTest, operationDtoForTransfer, logger);
        //assert
        Assertions.assertThrowsExactly(InvalidInputData.class, action,
                "Unknown input data (card number or date valid till or code CVV)");
    }

    public static Stream<Arguments> sourceMethodForCheckInvalidInputDataTest() {
        return Stream.of(
                Arguments.of(
                        new ClientCard("5555000055550000",
                                "12/25",
                                "123",
                                1000, "RUR")
                ),
                Arguments.of(
                        new ClientCard(
                                "2222111133334444",
                                "01/25",
                                "123",
                                1000, "RUR")
                ),
                Arguments.of(
                        new ClientCard(
                                "2222111133334444",
                                "12/25",
                                "000",
                                1000, "RUR")
                )
        );
    }

    @Test
    public void checkIsEmptyNumberFromTest() {
        //arrange
        String number = null;
        boolean expected = true;
        //act
        boolean actual = exceptionsServ.isEmpty(number);
        //assert
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("sourceMethodForCheckIsEmptyFieldsTest")
    public void checkIsEmptyFieldsTest(OperationDtoForTransfer withEmptyField) {
        //arrange
        Log logger = Mockito.mock(Log.class);
        ThrowingExceptionsService exceptionsServ = new ThrowingExceptionsService();
        //act
        Executable action = () -> exceptionsServ
                .checkEmptyFields(withEmptyField, logger);
        //assert
        Assertions.assertThrowsExactly(InvalidInputData.class, action,
                "Some card parameter (number or date valid till or code CVV) is empty");
    }

    public static Stream<Arguments> sourceMethodForCheckIsEmptyFieldsTest() {
        ClientCard cardFromTest = new ClientCard(
                "2222111133334444",
                "12/25",
                "000",
                1000, "RUR");
        String cardToNumberTest = "1111222233334444";
        return Stream.of(
                Arguments.of(
                        new OperationDtoForTransfer("",
                                cardFromTest.getValidTill(),
                                cardFromTest.getCodeCvv(),
                                cardToNumberTest, new Amount(400, "RUR"))
                ),
                Arguments.of(
                        new OperationDtoForTransfer(cardFromTest.getNumber(),
                                "",
                                cardFromTest.getCodeCvv(),
                                cardToNumberTest, new Amount(400, "RUR"))
                ),
                Arguments.of(
                        new OperationDtoForTransfer(cardFromTest.getNumber(),
                                cardFromTest.getValidTill(),
                                cardFromTest.getCodeCvv(),
                                "", new Amount(400, "RUR"))
                )
        );
    }

    @Test
    public void checkMinimalAmountTest() {
        //arrange
        Amount amountTest = new Amount(0, "RUR");
        //act
        Executable action = () -> exceptionsServ.checkMinimalAmount(amountTest, logger);
        //assert
        Assertions.assertThrowsExactly(InvalidInputData.class, action, "Minimal value should be more then 0");
    }

    @ParameterizedTest
    @MethodSource("sourceMethodForCheckClientCard")
    public void checkClientCardTest(String toOrFrom) {
        //arrange
        ClientCard cardFromTest = null;
        //act
        Executable action = () -> exceptionsServ.checkClientCard(cardFromTest, logger, toOrFrom);
        //assert
        Assertions.assertThrowsExactly(
                NotFoundException.class, action, "Card " + toOrFrom + " is not found");
    }

    public static Stream<Arguments> sourceMethodForCheckClientCard() {
        return Stream.of(Arguments.of("FROM"),
                Arguments.of("TO"));
    }

    @Test
    public void checkEnoughMoneyTest() {
        //arrange
        OperationDtoForTransfer operationDtoForTransfer =
                new OperationDtoForTransfer(
                        cardFrom.getNumber(),
                        cardFrom.getValidTill(),
                        cardFrom.getCodeCvv(),
                        cardToNumber,
                        new Amount(12000, "RUR")
                );
        //act
        Executable actual = () -> exceptionsServ.checkEnoughMoney(cardFrom, operationDtoForTransfer, logger);
        //assert
        Assertions.assertThrowsExactly(
                ExpenseOverBalance.class, actual, "Not enough money");
    }

    @Test
    public void checkConfirmCodeTest() {
        //arrange
        Operation operation = new Operation("1",
                cardFrom,
                cardToNumber,
                new Amount(200, "RUR"),
                new Amount(100, "RUR"),
                OperationStatus.CREATED,
                "111000");
        OperationDtoForConfirm operationDtoForConfirm = new OperationDtoForConfirm(
                operation.getId(), "12345");
        //act
        Executable actual = () -> exceptionsServ.checkConfirmCode(operationDtoForConfirm, operation,
                repository, logger);
        //assert
        Assertions.assertThrowsExactly(
                NonConfirmOperation.class, actual, "Operation isn't confirming");
    }

    @Test
    public void checkNotFoundOperationTest() {
        //arrange
        //act
        Executable actual = () -> exceptionsServ.checkNotFoundOperation("1", logger, repository);
        //assert
        Assertions.assertThrowsExactly(NotFoundException.class, actual, "Operation isn't created");
    }
}
