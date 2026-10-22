package miniwallet.repository;

import miniwallet.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Sort;

public interface WalletTransactionRepository
        extends JpaRepository<WalletTransaction, Long> {

    List<WalletTransaction> findByWalletId(Long walletId, Sort sort);
}