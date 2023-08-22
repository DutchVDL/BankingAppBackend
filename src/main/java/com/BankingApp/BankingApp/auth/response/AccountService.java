package com.BankingApp.BankingApp.auth.response;

import com.BankingApp.BankingApp.auth.TransactionCreationRequest;
import com.BankingApp.BankingApp.exceptions.InsufficientBalanceException;
import com.BankingApp.BankingApp.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
    public class AccountService {

        @Autowired
        private AccountRepository accountRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private TransactionsRepository transactionsRepository;

        // Method to perform a transaction between two accounts using account names
            public void performTransaction(String sourceAccountName,
                                           String targetAccountName,
                                           TransactionCreationRequest request) {
            BigDecimal amount = request.getTransaction();

            // Fetch the source and target accounts from the repository
            Account sourceAccount = accountRepository.findByAccountName(sourceAccountName);
            Account targetAccount = accountRepository.findByAccountName(targetAccountName);

            // Check if the source account exists and has sufficient balance for the transaction
            if (sourceAccount == null) {
                throw new IllegalArgumentException("Source account does not exist.");

            }
            if (sourceAccount == targetAccount) {
                    throw new IllegalArgumentException("You can't make a transaction to the same account");

                }


            else if (sourceAccount.getBalance().compareTo(amount) >= 0) {
//                 Subtract the amount from the source account's balance
                sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));

                // Add the amount to the target account's balance
                targetAccount.setBalance(targetAccount.getBalance().add(amount));

                // Save the updated account balances
                accountRepository.save(sourceAccount);
                accountRepository.save(targetAccount);



                TransactionEntry sourceTransaction = new TransactionEntry();
                sourceTransaction.setBalance(amount.negate()); // Set the balance with a negative sign
                sourceTransaction.setTimestamp(LocalDateTime.now());
                sourceTransaction.setType(TransactionType.TRANSFER);

                TransactionEntry targetTransaction = new TransactionEntry();
                targetTransaction.setBalance(amount); // Set the balance without a negative sign (it will be positive)
                targetTransaction.setTimestamp(LocalDateTime.now());
                targetTransaction.setType(TransactionType.TRANSFER);



                targetAccount.addTransaction(targetTransaction);

                // Save the subtracted transaction

                sourceAccount.addTransaction(sourceTransaction);

                // Save the updated account balances
                accountRepository.save(sourceAccount);
                accountRepository.save(targetAccount);

                // Save the transaction to the repository
                transactionsRepository.save(sourceTransaction);
                transactionsRepository.save(targetTransaction);


            } else {
                throw new InsufficientBalanceException("Insufficient balance in the source account.");
            }
        }




            // Method to deposit money into an account using the account name
          public void depositMoney(String accountName, TransactionCreationRequest request) {
//

                 BigDecimal amount = request.getTransaction();

                // Fetch the account from the repository based on the account name
                Account account = accountRepository.findByAccountName(accountName);

             // Check if the account exists
            if (account == null) {
                throw new IllegalArgumentException("Account does not exist.");
             }

        // Add the amount to the account's balance
            account.setBalance(account.getBalance().add(amount));

            // Save the updated account balance
            accountRepository.save(account);

            // Create a new transaction entity
               TransactionEntry transaction = new TransactionEntry();
               transaction.setBalance(amount);
               transaction.setTimestamp(LocalDateTime.now());
               transaction.setType(request.getTransactionType());

        // Associate the transaction with the account
               transaction.addAccount(account);

        // Save the transaction to the repository
               transactionsRepository.save(transaction);


    }

        public Optional<Account> getAccountById(Long id) {
                Optional<Account> account = accountRepository.findById(id);
                return account;
        }

        public String deleteAccountById(Long id){


                accountRepository.deleteById(id);
                return "Account has been deleted";

        }


        public void withdrawMoney(String accountName, TransactionCreationRequest request){
            BigDecimal amount = request.getTransaction();
            Account account = accountRepository.findByAccountName(accountName);
            if (account.getBalance().compareTo(amount)<0) {
                throw new IllegalArgumentException("Not enough Funds");
            }
            account.setBalance(account.getBalance().subtract(amount));
            accountRepository.save(account);
            TransactionEntry transaction = new TransactionEntry();
            transaction.setBalance(amount.negate());
            transaction.setTimestamp(LocalDateTime.now());
            transaction.setType(request.getTransactionType());
            transaction.addAccount(account);
            transactionsRepository.save(transaction);

        }

        public List<Account> fetchAccounts(String email){
            Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found")));
            return user.get().getAccounts();
        }
    }
