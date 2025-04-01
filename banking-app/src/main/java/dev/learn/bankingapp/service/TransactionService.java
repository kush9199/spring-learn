package dev.learn.bankingapp.service;

import dev.learn.bankingapp.constants.TRANSACTION_TYPE;
import dev.learn.bankingapp.dto.TransactionRequest;
import dev.learn.bankingapp.dto.TransactionResponse;
import dev.learn.bankingapp.entity.Account;
import dev.learn.bankingapp.entity.Transaction;
import dev.learn.bankingapp.reposiotry.AccountRepository;
import dev.learn.bankingapp.reposiotry.TransactionRepository;
import dev.learn.bankingapp.reposiotry.TransferRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository,
            TransferRepository transferRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    @Transactional
    public TransactionResponse withdraw(TransactionRequest transaction) {
        Transaction statement = withdraw(transaction.accountNumber, transaction.amount);
        return new TransactionResponse(statement);
    }

    @Transactional
    public TransactionResponse deposit(TransactionRequest transaction) {
        Transaction statement = deposit(transaction.accountNumber, transaction.amount);
        return new TransactionResponse(statement);
    }

    @Transactional
    public TransactionResponse transfer(TransactionRequest transaction) {
        Transaction statement = transfer(
                transaction.fromAccount,
                transaction.toAccount,
                transaction.amount);
        return new TransactionResponse(statement);
    }

    @Transactional
    public Transaction deposit(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.deposit(amount);
        Account savedAccount = accountRepository.save(account);
        Transaction transaction = new Transaction();
        transaction.setBalance(savedAccount.getBalance());
        transaction.setFromAccount(account);
        transaction.setType(TRANSACTION_TYPE.DEPOSIT);
        transaction.setTimestamp(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction withdraw(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.withdraw(amount);
        Account savedAccount = accountRepository.save(account);
        Transaction transaction = new Transaction();
        transaction.setBalance(savedAccount.getBalance());
        transaction.setFromAccount(account);
        transaction.setType(TRANSACTION_TYPE.WITHDRAWAL);
        transaction.setTimestamp(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("From Account not found"));
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        Transaction transaction = Transaction.transfer(fromAccount, toAccount, amount);
        return transferRepository.transferMoney(transaction);
    }

}
