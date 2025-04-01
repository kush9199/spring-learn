package dev.learn.bankingapp.dto;

import dev.learn.bankingapp.constants.TRANSACTION_TYPE;
import dev.learn.bankingapp.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {
    public String accountNumber;
    public String toAccount;
    public BigDecimal amount;
    public TRANSACTION_TYPE transactionType;
    public LocalDateTime timestamp;

    public TransactionResponse(Transaction transaction) {
        this.accountNumber = transaction.getFromAccount().getAccountNumber();
        this.toAccount = transaction.getToAccount() != null ? transaction.getToAccount().getAccountNumber() : null;
        this.amount = transaction.getBalance();
        this.transactionType = transaction.getType();
        this.timestamp = transaction.getTimestamp();
    }
}
