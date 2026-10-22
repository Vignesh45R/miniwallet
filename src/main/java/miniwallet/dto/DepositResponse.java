package miniwallet.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class DepositResponse {

    private Long walletId;
    private BigDecimal previousBalance;
    private BigDecimal amount;
    private BigDecimal currentBalance;
}