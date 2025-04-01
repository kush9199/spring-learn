package dev.learn.bankingapp.controller;

import dev.learn.bankingapp.dto.ErrorResponse;
import dev.learn.bankingapp.dto.TransactionRequest;
import dev.learn.bankingapp.dto.TransactionResponse;
import dev.learn.bankingapp.service.AccountService;
import dev.learn.bankingapp.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final AccountService accountService;

    public TransactionController(TransactionService transactionService, AccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody TransactionRequest transaction) {
        if(transaction.accountNumber == null){
            ErrorResponse err = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "body is null",
                    LocalDateTime.now()
            );
            return ResponseEntity.badRequest().body(err);
        }

        if(transaction.accountNumber.isEmpty()
                || !(transaction.fromAccount.isEmpty() && transaction.toAccount.isEmpty())){
            ErrorResponse err = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "not valid credentials",
                    LocalDateTime.now()
            );
            return ResponseEntity.badRequest().body(err);
        }
        if(!accountService.existsByAccountNumber(transaction.accountNumber)){
            ErrorResponse err = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "account don't exists",
                    LocalDateTime.now()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
        }
        TransactionResponse resp = transactionService.deposit(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody TransactionRequest transaction) {
        if(transaction.accountNumber == null){
            ErrorResponse err = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "body is null",
                    LocalDateTime.now()
            );
            return ResponseEntity.badRequest().body(err);
        }

        if(transaction.accountNumber.isEmpty()
                || !(transaction.fromAccount.isEmpty() && transaction.toAccount.isEmpty())){
            ErrorResponse err = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "not valid credentials",
                    LocalDateTime.now()
            );
            return ResponseEntity.badRequest().body(err);
        }
        if(!accountService.existsByAccountNumber(transaction.accountNumber)){
            ErrorResponse err = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "account don't exists",
                    LocalDateTime.now()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
        }
        TransactionResponse resp = transactionService.withdraw(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransactionRequest transaction) {
        if(transaction.accountNumber == null){
            ErrorResponse err = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "body is null",
                    LocalDateTime.now()
            );
            return ResponseEntity.badRequest().body(err);
        }

        if(!transaction.accountNumber.isEmpty()) {
            ErrorResponse err = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "not valid credentials",
                    LocalDateTime.now()
            );
            return ResponseEntity.badRequest().body(err);
        }
        if(transaction.fromAccount.isEmpty() ||
                transaction.toAccount.isEmpty() ||
                transaction.fromAccount.equalsIgnoreCase(transaction.toAccount)){
            ErrorResponse err = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "not valid credentials",
                    LocalDateTime.now()
            );
            return ResponseEntity.badRequest().body(err);
        }

        if(!accountService.existsByAccountNumber(transaction.fromAccount) ||
                !accountService.existsByAccountNumber(transaction.toAccount)){
            ErrorResponse err = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "account don't exists",
                    LocalDateTime.now()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
        }
        TransactionResponse resp = transactionService.transfer(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
