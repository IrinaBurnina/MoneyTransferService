package ir.bu.moneytransferservice.confirmTransfer;

import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

public class VerificationCode {
    @Getter
    private String code;

    public VerificationCode() {
        int codeRandom = ThreadLocalRandom.current().nextInt(1000, 10000);
        this.code = String.valueOf(codeRandom);
    }
}
