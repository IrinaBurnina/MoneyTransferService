package ir.bu.moneytransferservice.confirmTransfer;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@Component
public class VerificationCode {
    private final String code;

    public VerificationCode() {
        int codeRandom = ThreadLocalRandom.current().nextInt(1000, 10000);
        this.code = String.valueOf(codeRandom);
    }
}
