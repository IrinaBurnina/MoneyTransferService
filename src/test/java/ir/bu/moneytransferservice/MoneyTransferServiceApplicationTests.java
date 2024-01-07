package ir.bu.moneytransferservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
class MoneyTransferServiceApplicationTests {

    public static void main(String[] args) {
        SpringApplication.from(MoneyTransferServiceApplication::main).with(MoneyTransferServiceApplicationTests.class).run(args);
    }

}
