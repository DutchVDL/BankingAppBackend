package com.BankingApp.BankingApp.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_account")
public class Account {

    public Account(String name) {
        this.accountName = name;

        this.balance = BigDecimal.ZERO;
        this.transactions = new ArrayList<>();
    }

    @Id
    @GeneratedValue
    private Long Id;

    private String accountName;
    private BigDecimal balance;


    @JsonIgnoreProperties("accounts")
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "account_transaction",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "transaction_id")
    )
    private List<TransactionEntry> transactions;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;


    public void addTransaction(TransactionEntry transaction) {
        this.transactions.add(transaction);
        transaction.getAccounts().add(this);
    }

}
