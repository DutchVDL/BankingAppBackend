package com.BankingApp.BankingApp.auth;

import com.BankingApp.BankingApp.auth.response.AccountService;
import com.BankingApp.BankingApp.exceptions.InsufficientBalanceException;
import com.BankingApp.BankingApp.user.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class AccountController {

    @Autowired
    private AccountService accountService;




    @PostMapping("/accounts/transfer")
    public ResponseEntity<String> performTransaction(@RequestBody MoneyTransferRequest request) {
        try {
            accountService.performTransaction(request.getAccountOne(), request.getAccountTwo(), request.getTransactionCreationRequest());
            return ResponseEntity.ok("Transaction successful.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/accounts/deposit/{accountName}")
    public ResponseEntity<TransactionCreationRequest> depositMoney(@PathVariable("accountName") String accountName,
                                               @RequestBody TransactionCreationRequest request) {


        try {
            request.setTimestamp(LocalDateTime.now());
            accountService.depositMoney(accountName, request);
            return ResponseEntity.ok(request);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/accounts/withdraw/{accountName}")
    public ResponseEntity<TransactionCreationRequest> withdrawMoney(
            @PathVariable("accountName") String accountName,  @RequestBody TransactionCreationRequest request){
        try {
            request.setTimestamp(LocalDateTime.now());
            accountService.withdrawMoney(accountName, request);
            return ResponseEntity.ok(request);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Optional<Account>> getAccountById(@PathVariable("id") Long id) {
        Optional<Account> account = accountService.getAccountById(id);
        if (account == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(account);
        }
    }

    @GetMapping("/allAccounts")
    public List<Account> fetchAccounts(String email){
        return  accountService.fetchAccounts(email);
    }

    @DeleteMapping("/accounts/{id}")
    public String deleteAccountById(@PathVariable("id") Long id){
        accountService.deleteAccountById(id);
        return "Account has been deleted";
    }

}
