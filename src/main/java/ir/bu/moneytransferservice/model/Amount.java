package ir.bu.moneytransferservice.model;

public record Amount(int value, String currency) {
    @Override
    public int value() {
        return value;
    }

    @Override
    public String currency() {
        return currency;
    }
}
