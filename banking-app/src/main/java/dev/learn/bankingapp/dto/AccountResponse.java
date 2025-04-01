package dev.learn.bankingapp.dto;

import java.math.BigDecimal;

public class AccountResponse {
    public String accountNumber;
    public BigDecimal balance;
    public String currency;
    public AccountResponse(String accountNumber, BigDecimal balance, String currency) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.currency = currency;
    }
}
