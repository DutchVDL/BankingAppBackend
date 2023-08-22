package com.BankingApp.BankingApp.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface TransactionsRepository extends JpaRepository<TransactionEntry, Long> {
    Optional<TransactionEntry> findByBalance(BigDecimal balance);
}
