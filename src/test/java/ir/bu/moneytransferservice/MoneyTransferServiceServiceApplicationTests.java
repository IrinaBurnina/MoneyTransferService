package ir.bu.moneytransferservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
class MoneyTransferServiceServiceApplicationTests {

    public static void main(String[] args) {
        SpringApplication.from(MoneyTransferServiceApplication::main).with(MoneyTransferServiceServiceApplicationTests.class).run(args);
    }

}
