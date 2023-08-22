package com.BankingApp.BankingApp.auth;


import com.BankingApp.BankingApp.user.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCreationRequest {

    private BigDecimal transaction;
    private LocalDateTime timestamp;
    private TransactionType transactionType;
}
