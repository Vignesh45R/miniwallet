package miniwallet.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class WalletResponse {

    private Long walletId;
    private String name;
    private String email;
    private BigDecimal balance;
}