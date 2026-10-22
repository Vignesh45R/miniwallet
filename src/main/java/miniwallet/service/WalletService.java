package miniwallet.service;

import miniwallet.entity.Wallet;
import miniwallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import org.springframework.transaction.annotation.Transactional;
import miniwallet.dto.DepositResponse;
import miniwallet.entity.WalletTransaction;
import miniwallet.repository.WalletTransactionRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import miniwallet.dto.TransactionResponse;
import miniwallet.dto.TransferRequest;
import miniwallet.dto.TransferResponse;
import miniwallet.exception.WalletNotFoundException;
import miniwallet.exception.DuplicateEmailException;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    public WalletService(
            WalletRepository walletRepository,
            WalletTransactionRepository walletTransactionRepository) {

        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }



    @Transactional
    public Wallet createWallet(
            String name, String email, BigDecimal initialBalance) {

        if (walletRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email already exists");
        }

        Wallet wallet = new Wallet(name, email);
        wallet.setBalance(initialBalance);

        Wallet savedWallet = walletRepository.save(wallet);

        if (initialBalance.compareTo(BigDecimal.ZERO) > 0) {
            WalletTransaction transaction = new WalletTransaction();
            transaction.setWallet(savedWallet);
            transaction.setAmount(initialBalance);
            transaction.setType("INITIAL_DEPOSIT");

            walletTransactionRepository.save(transaction);
        }

        return savedWallet;
    }


    public Wallet getWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() ->
                        new WalletNotFoundException(
                                "Wallet not found with ID: " + walletId
                        )
                );
    }

    @Transactional
    public DepositResponse deposit(Long walletId, BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Deposit amount must be greater than zero"
            );
        }

        Wallet wallet = getWalletById(walletId);

        BigDecimal previousBalance = wallet.getBalance();

        BigDecimal currentBalance = previousBalance.add(amount);

        wallet.setBalance(currentBalance);

        walletRepository.save(wallet);

        WalletTransaction transaction = new WalletTransaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setType("DEPOSIT");

        walletTransactionRepository.save(transaction);

        DepositResponse response = new DepositResponse();
        response.setWalletId(wallet.getId());
        response.setPreviousBalance(previousBalance);
        response.setAmount(amount);
        response.setCurrentBalance(currentBalance);

        return response;
    }

    public List<TransactionResponse> getTransactions(Long walletId) {

        getWalletById(walletId);

        List<WalletTransaction> transactions =
                walletTransactionRepository.findByWalletId(
                        walletId,
                        Sort.by(Sort.Direction.DESC, "createdAt")
                );

        return transactions.stream()
                .map(transaction -> {
                    TransactionResponse response = new TransactionResponse();

                    response.setTransactionId(transaction.getId());
                    response.setWalletId(transaction.getWallet().getId());
                    response.setAmount(transaction.getAmount());
                    response.setType(transaction.getType());
                    response.setCreatedAt(transaction.getCreatedAt());

                    return response;
                })
                .toList();
    }

    @Transactional
    public TransferResponse transfer(TransferRequest request) {

        Long fromWalletId = request.getFromWalletId();
        Long toWalletId = request.getToWalletId();
        BigDecimal amount = request.getAmount();

        if (fromWalletId.equals(toWalletId)) {
            throw new IllegalArgumentException(
                    "Cannot transfer money to the same wallet"
            );
        }

        Wallet fromWallet = getWalletById(fromWalletId);
        Wallet toWallet = getWalletById(toWalletId);

        if (fromWallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException(
                    "Insufficient wallet balance"
            );
        }

        BigDecimal fromBalance =
                fromWallet.getBalance().subtract(amount);

        BigDecimal toBalance =
                toWallet.getBalance().add(amount);

        fromWallet.setBalance(fromBalance);
        toWallet.setBalance(toBalance);

        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);

        WalletTransaction outgoing = new WalletTransaction();
        outgoing.setWallet(fromWallet);
        outgoing.setAmount(amount);
        outgoing.setType("TRANSFER_OUT");

        WalletTransaction incoming = new WalletTransaction();
        incoming.setWallet(toWallet);
        incoming.setAmount(amount);
        incoming.setType("TRANSFER_IN");

        walletTransactionRepository.save(outgoing);
        walletTransactionRepository.save(incoming);

        TransferResponse response = new TransferResponse();
        response.setFromWalletId(fromWalletId);
        response.setToWalletId(toWalletId);
        response.setAmount(amount);
        response.setFromWalletBalance(fromBalance);
        response.setToWalletBalance(toBalance);

        return response;
    }
}