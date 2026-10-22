package miniwallet.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class TransferResponse {

    private Long fromWalletId;
    private Long toWalletId;
    private BigDecimal amount;
    private BigDecimal fromWalletBalance;
    private BigDecimal toWalletBalance;
}