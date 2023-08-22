package com.BankingApp.BankingApp.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {


    Account findByAccountName(String sourceAccountName);

    List<Account> findByUserEmail(String email);

    Optional<Account> findById(Long id);
}
