package ir.bu.moneytransferservice;

import ir.bu.moneytransferservice.model.Amount;
import ir.bu.moneytransferservice.model.dto.OperationDtoForConfirm;
import ir.bu.moneytransferservice.model.dto.OperationDtoForTransfer;
import ir.bu.moneytransferservice.model.dto.OperationDtoResponses;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TestContainersApplicationTests {
    private static final GenericContainer<?> myAppMts = new GenericContainer<>("myapp:latest")
            .withExposedPorts(5500);

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    static void setUp() {
        myAppMts.start();
    }

    @Test
    void contextLoads() {

    }

    @Test
    public void myAppMtsTransferTest() {
        //arrange
        OperationDtoForTransfer operationDtoForTransfer = new OperationDtoForTransfer(
                "2222111133334444", "1111222233334444",
                "123", "12/25",
                new Amount("RUR", 100));
        String url = "http://localhost:" + myAppMts.getMappedPort(5500) + "/transfer/transfer";
        OperationDtoResponses expected = new OperationDtoResponses("1");
        //act
        ResponseEntity<OperationDtoResponses> entityFromMyAppMts = restTemplate.postForEntity(url,
                operationDtoForTransfer,
                OperationDtoResponses.class);
        OperationDtoResponses actual = entityFromMyAppMts.getBody();
        assert actual != null;
        //assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void myAppMtsConfirmTest() {
        //arrange
        OperationDtoForTransfer operationDtoForTransfer = new OperationDtoForTransfer(
                "2222111133334444", "1111222233334444",
                "123", "12/25",
                new Amount("RUR", 100));
        String urlTransfer = "http://localhost:" + myAppMts.getMappedPort(5500) + "/transfer";
        ResponseEntity<OperationDtoResponses> entityFromMyAppMts = restTemplate.postForEntity(urlTransfer,
                operationDtoForTransfer,
                OperationDtoResponses.class);
        OperationDtoResponses operationResponse = entityFromMyAppMts.getBody();
        assert operationResponse != null;
        OperationDtoForConfirm operationDtoForConfirm = new OperationDtoForConfirm(operationResponse.getOperationId(), "0000");
        OperationDtoResponses expected = new OperationDtoResponses(operationResponse.getOperationId());
        String urlConfirm = "http://localhost:" + myAppMts.getMappedPort(5500) + "/confirmOperation";
        //act
        ResponseEntity<OperationDtoResponses> entityForConfirming = restTemplate.postForEntity(urlConfirm,
                operationDtoForConfirm,
                OperationDtoResponses.class);
        OperationDtoResponses actual = entityForConfirming.getBody();
        //assert
        Assertions.assertEquals(expected, actual);
    }
}
