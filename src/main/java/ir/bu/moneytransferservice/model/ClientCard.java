package ir.bu.moneytransferservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
@ToString
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
    private int balance;
    private final String currency;
}
