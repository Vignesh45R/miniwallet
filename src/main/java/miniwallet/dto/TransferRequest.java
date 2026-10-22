package miniwallet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class TransferRequest {

    @NotNull
    private Long fromWalletId;

    @NotNull
    private Long toWalletId;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false,
            message = "Transfer amount must be greater than zero")
    private BigDecimal amount;
}