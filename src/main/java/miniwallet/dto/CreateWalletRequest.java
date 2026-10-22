package miniwallet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateWalletRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotNull(message = "Initial balance is required")
    @DecimalMin(
        value = "0.00",
        message = "Initial balance cannot be negative"
    )
    private BigDecimal initialBalance;
}