package ir.bu.moneytransferservice.model.dto;

import ir.bu.moneytransferservice.model.Amount;
import lombok.Getter;
import lombok.Setter;

@Getter
public class OperationDtoForTransfer {
    @Setter
    private String cardFromNumber;
    @Setter
    private String cardToNumber;
    @Setter
    private Amount amount;
    @Setter
    private String cardFromValidTill;
    @Setter
    private String cardFromCVV;

    public OperationDtoForTransfer(String cardFromNumber, String cardToNumber, String cardFromCVV,
                                   String cardFromValidTill, Amount amount) {
        this.cardFromNumber = cardFromNumber;
        this.cardToNumber = cardToNumber;
        this.amount = amount;
        this.cardFromValidTill = cardFromValidTill;
        this.cardFromCVV = cardFromCVV;
    }

    @Override
    public String toString() {
        return "OperationDtoForTransfer{" +
                "cardFromNumber='" + cardFromNumber + '\'' +
                ", cardToNumber='" + cardToNumber + '\'' +
                ", amount=" + amount +
                ", cardFromValidTill='" + cardFromValidTill + '\'' +
                ", cardFromCVV='" + cardFromCVV + '\'' +
                '}';
    }
}
