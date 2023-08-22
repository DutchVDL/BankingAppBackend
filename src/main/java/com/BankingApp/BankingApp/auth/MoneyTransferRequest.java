package com.BankingApp.BankingApp.auth;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MoneyTransferRequest {
    private String accountOne;
    private String accountTwo;

    private TransactionCreationRequest transactionCreationRequest;


}
