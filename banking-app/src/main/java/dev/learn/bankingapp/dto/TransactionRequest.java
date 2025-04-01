package dev.learn.bankingapp.dto;

import java.math.BigDecimal;

public class TransactionRequest {
    public String accountNumber; // Used for deposit/withdraw
    public String fromAccount; // Used for transfers
    public String toAccount; // Used for transfers
    public BigDecimal amount;

    public TransactionRequest(String accountNumber,
                              String fromAccount,
                              String toAccount,
                              BigDecimal amount) {
        this.accountNumber = accountNumber;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }
}
