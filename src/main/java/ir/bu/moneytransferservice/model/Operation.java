package ir.bu.moneytransferservice.model;

import lombok.*;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class Operation {
    @Setter
    private final String id;
    private final ClientCard cardFromNumber;
    private final String cardToNumber;
    private final Amount amount;
    @Setter
    private Amount fee;
    @Setter
    private OperationStatus operationStatus;
    @Setter
    private String verificationCode;
}
