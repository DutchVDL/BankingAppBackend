package com.BankingApp.BankingApp.auth;

import com.BankingApp.BankingApp.auth.response.Response;
import com.BankingApp.BankingApp.config.JwtService;
import com.BankingApp.BankingApp.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private  final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    public AuthenticationResponse register(RegisterRequest request) {

        if (repository.existsByNickname(request.getUsername())) {
            throw new RuntimeException("Username already exists. Please choose a different username.");
        }

        // Check if the email already exists in the repository
        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists. Please use a different email address.");
        }


        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getUsername())
                .role(Role.USER)
                .build();
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return  AuthenticationResponse.builder()
                .token(jwtToken)
                .build();


    }





    public Response authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();


        var jwtToken = jwtService.generateToken(user);
          AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

          Response response  = new Response(user,jwtToken);

          return response;
    }




    public UserDetails createAccount(String name, String email) {



        var user = repository.findByEmail(email).get();


        if (user.getAccounts()
                .stream()
                .map(account -> account.getAccountName().equalsIgnoreCase(name))
                .anyMatch(Boolean::booleanValue)) {
            throw new RuntimeException("Account already exists. Please choose a different name.");
        }



        user.createAccount(name);
        repository.save(user);
        return user;
    }

    public List<Account> getAllAccounts(String email){
        var user = repository.findByEmail(email);
        if (user == null) {
            // Handle the case when the user is not found
            throw new RuntimeException("User not found");
        }
        List<Account> allAccounts = user.get().getAccounts();
        return  allAccounts;
    }


    //Get User

    public User getUserByEmail(String email) {
        var user = repository.findByEmail(email);
        if (user.isPresent()) {

                return user.get();
            } else {
                // Password doesn't match, throw an exception or handle as appropriate
                throw new RuntimeException("Invalid credentials");
            }

    }



    public List<Account> getAccountsByUserEmail(String email) {
        return accountRepository.findByUserEmail(email);
    }
}
