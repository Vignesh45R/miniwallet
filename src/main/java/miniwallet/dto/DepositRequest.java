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
public class DepositRequest {

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false,
            message = "Deposit amount must be greater than zero")
    private BigDecimal amount;
}