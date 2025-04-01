package dev.learn.bankingapp.controller;

import dev.learn.bankingapp.dto.AccountRequest;
import dev.learn.bankingapp.dto.AccountResponse;
import dev.learn.bankingapp.dto.ErrorResponse;
import dev.learn.bankingapp.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody AccountRequest account) {
        if(account.username == null) {
            ErrorResponse resp = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "empty body",
                    LocalDateTime.now()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        }
        if(account.username.isEmpty() || account.currency.isEmpty()) {
            ErrorResponse resp = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "empty fields",
                    LocalDateTime.now()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        }
        if(accountService.findAccountByUsername(account.username)){
            ErrorResponse resp = new ErrorResponse(
                    HttpStatus.CONFLICT.value(),
                    "account already exists",
                    LocalDateTime.now()
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resp);
        }
        if(!accountService.findUserByUsername(account.username)){
            ErrorResponse resp = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "no user found",
                    LocalDateTime.now()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }
        AccountResponse resp = accountService.addAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getAccount(@PathVariable String accountNumber) {
        if(accountService.existsByAccountNumber(accountNumber)){
            AccountResponse resp = accountService.findAccountByAccountNumber(accountNumber);
            return ResponseEntity.status(HttpStatus.OK).body(resp);
        }
        ErrorResponse resp = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "account not found", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<?> deleteAccount(@PathVariable String accountNumber) {
        if(accountService.existsByAccountNumber(accountNumber)){
            accountService.deleteAccountByAccountNumber(accountNumber);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        ErrorResponse resp = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "account not found", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
    }
}
