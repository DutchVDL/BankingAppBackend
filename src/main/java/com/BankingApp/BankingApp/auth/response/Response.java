package com.BankingApp.BankingApp.auth.response;

import com.BankingApp.BankingApp.user.Account;
import com.BankingApp.BankingApp.user.Role;
import com.BankingApp.BankingApp.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private String token;
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
//    private String password;
    private List<Account> accounts;

    //new
    private String nickname;
    //


    private Role role;

    public Response(User user, String jwtToken) {
        this.token = jwtToken;
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.role = user.getRole();
        this.accounts = user.getAccounts();
        this.nickname = user.getNickname();
    }
}
