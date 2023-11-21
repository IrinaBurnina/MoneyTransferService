package ir.bu.moneytransferservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Setter;

import java.util.Objects;

public class ClientCard {
    @NotBlank
    @Size(min = 16, max = 16)
    private final String number;
    @NotBlank
    private final String validTill;
    @NotBlank
    @Size(min = 3, max = 3)
    private final String codeCvv;
    @Setter
    private int balance;//TODO вывод на рубли, а не на копейки
    private final String currency;

    public ClientCard(String number, String validTill, String codeCvv, int balance, String currency) {
        this.number = number;
        this.validTill = validTill;
        this.codeCvv = codeCvv;
        this.balance = balance;
        this.currency = currency;
    }

    public String getNumber() {
        return number;
    }

    public String getValidTill() {
        return validTill;
    }

    public String getCodeCvv() {
        return codeCvv;
    }

    public int getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientCard that = (ClientCard) o;
        return Objects.equals(number, that.number) && Objects.equals(validTill, that.validTill) && Objects.equals(codeCvv, that.codeCvv) && Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, validTill, codeCvv, currency);
    }

    @Override
    public String toString() {
        return "ClientCard{" +
                "number='" + number + '\'' +
                ", balance=" + balance +
                ", currency='" + currency + '\'' +
                '}';
    }
}
