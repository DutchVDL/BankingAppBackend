package com.BankingApp.BankingApp.auth;


import com.BankingApp.BankingApp.auth.response.Response;
import com.BankingApp.BankingApp.user.Account;
import com.BankingApp.BankingApp.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    @Autowired
    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){

    return  ResponseEntity.ok(service.register(request));

    }

//    @PostMapping("/authenticate")
//    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request){
//
//        return  ResponseEntity.ok(service.authenticate(request));
//    }


    @PostMapping("/authenticate")
    public ResponseEntity<Response> login(@RequestBody AuthenticationRequest request){

        return  ResponseEntity.ok(service.authenticate(request));
    }


    @PostMapping ("/accounts")
    public ResponseEntity<UserDetails> createAccount(@RequestBody AccountCreationRequest request) {
        UserDetails userDetails = service.createAccount(request.getAccName(), request.getEmail());
        return ResponseEntity.ok(userDetails);
    }

    //new methods

    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = service.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            // Do not include the password in the response for security reasons

            return ResponseEntity.ok(user);
        }
    }






    @GetMapping("/accounts/{email}")
    @CrossOrigin
    public List<Account> getAccountsByUserEmail(@PathVariable String email) {
        return service.getAccountsByUserEmail(email);
    }
}
