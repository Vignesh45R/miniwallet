package miniwallet.controller;

import miniwallet.service.WalletService;
import org.springframework.web.bind.annotation.RestController;
import miniwallet.entity.Wallet;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import miniwallet.dto.CreateWalletRequest;
import miniwallet.dto.WalletResponse;
import miniwallet.dto.DepositRequest;
import miniwallet.dto.DepositResponse;
import miniwallet.dto.TransactionResponse;
import miniwallet.dto.TransferRequest;
import miniwallet.dto.TransferResponse;
import java.util.List;

@RestController
@RequestMapping("/api/wallets")
@Validated
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }


    @PostMapping
    public WalletResponse createWallet(@Valid @RequestBody CreateWalletRequest request) {

        Wallet wallet = walletService.createWallet(
                request.getName(),
                request.getEmail(),
                request.getInitialBalance()
        );

        WalletResponse response = new WalletResponse();
        response.setWalletId(wallet.getId());
        response.setName(wallet.getName());
        response.setEmail(wallet.getEmail());
        response.setBalance(wallet.getBalance());

        return response;
    }


    @GetMapping("/{walletId}")
    public WalletResponse getWalletById(@PathVariable Long walletId) {

        Wallet wallet = walletService.getWalletById(walletId);

        WalletResponse response = new WalletResponse();
        response.setWalletId(wallet.getId());
        response.setName(wallet.getName());
        response.setEmail(wallet.getEmail());
        response.setBalance(wallet.getBalance());

        return response;
    }

    @PostMapping("/deposit/{walletId}")
    public DepositResponse deposit(
            @PathVariable Long walletId,
            @Valid @RequestBody DepositRequest request) {

        return walletService.deposit(walletId, request.getAmount());
    }

    @GetMapping("/transactions/{walletId}")
    public List<TransactionResponse> getTransactions(
            @PathVariable Long walletId) {

        return walletService.getTransactions(walletId);
    }

    @PostMapping("/transfer")
    public TransferResponse transfer(
            @Valid @RequestBody TransferRequest request) {

        return walletService.transfer(request);
    }
}