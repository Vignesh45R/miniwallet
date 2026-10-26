package miniwallet.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TransactionResponse {

    private Long transactionId;
    private Long walletId;
    private BigDecimal amount;
    private String type;
    private LocalDateTime createdAt;
    private String status;
    private String referenceId;
}