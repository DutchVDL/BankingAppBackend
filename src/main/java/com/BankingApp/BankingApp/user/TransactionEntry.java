package com.BankingApp.BankingApp.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEntry {



    @Id
    @GeneratedValue
    private Long id;


    private BigDecimal balance;
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToMany(mappedBy = "transactions")

    private List<Account> accounts = new ArrayList<>();


    public void addAccount(Account account) {
        this.accounts.add(account);
        account.getTransactions().add(this);
    }

}
