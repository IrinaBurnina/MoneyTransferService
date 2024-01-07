package ir.bu.moneytransferservice.dto;

import ir.bu.moneytransferservice.model.Amount;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class OperationDtoForTransfer {
    private String cardFromNumber;
    private String cardFromValidTill;
    private String cardFromCVV;
    private String cardToNumber;
    private Amount amount;
}

