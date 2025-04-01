package dev.learn.bankingapp.dto;

import dev.learn.bankingapp.constants.ACCOUNT_TYPE;

public class AccountRequest {
    public String username;
    public String currency;
    public ACCOUNT_TYPE accountType;
    public AccountRequest(String username, String currency, ACCOUNT_TYPE accountType) {
        this.username = username;
        this.currency = currency;
        this.accountType = accountType;
    }
}
