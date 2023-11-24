package ir.bu.moneytransferservice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public class Amount {

    private final int value;
    @Setter
    private String currency;

    public Amount(String currency, int value) {
        this.value = value;
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amount amount1 = (Amount) o;
        return value == amount1.value && Objects.equals(currency, amount1.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, currency);
    }

    @Override
    public String toString() {
        return "Amount{" +
                "amount=" + value +
                ", currency='" + currency + '\'' +
                '}';
    }
}
