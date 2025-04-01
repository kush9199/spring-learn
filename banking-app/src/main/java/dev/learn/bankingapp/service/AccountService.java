package dev.learn.bankingapp.service;

import dev.learn.bankingapp.dto.AccountRequest;
import dev.learn.bankingapp.dto.AccountResponse;
import dev.learn.bankingapp.entity.Account;
import dev.learn.bankingapp.entity.User;
import dev.learn.bankingapp.reposiotry.AccountRepository;
import dev.learn.bankingapp.reposiotry.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public String generateAccountNumber(String username, LocalDateTime date){
        StringBuilder accountNumber = new StringBuilder();

        String year = String.valueOf(date.getYear());
        String day = "0" + date.getDayOfWeek().getValue();
        String secs;
        if(String.valueOf(date.getSecond()).length() == 1){
            secs = "0" + date.getSecond();
        }else{
            secs = String.valueOf(date.getSecond());
        }
        StringBuilder initials = new StringBuilder();
        for(char ch : username.toUpperCase().strip().substring(0,4).toCharArray()){
            initials.append((int) ch);
        }
        accountNumber.append(year).append(day).append(secs).append(initials);
        return accountNumber.toString();
    }

    public AccountResponse addAccount (AccountRequest accountRequest){
        User user = userRepository.findByUsername(accountRequest.username)
                .orElseThrow(IllegalArgumentException::new);
        Account account = new Account();
        account.setUser(user);
        account.setType(accountRequest.accountType);
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency(accountRequest.currency);
        account.setAccountNumber(generateAccountNumber(accountRequest.username, LocalDateTime.now()));
        Account savedAccount = accountRepository.save(account);
        user.getAccounts().add(account);
        userRepository.save(user);
        return new AccountResponse(
                savedAccount.getAccountNumber(),
                savedAccount.getBalance(),
                savedAccount.getCurrency()
        );

    }

    public boolean findAccountByUsername(String username) {
        return accountRepository.existsAccountByUser_Username(username);
    }

    public boolean findUserByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByAccountNumber(String accountNumber) {
        return accountRepository.existsAccountByAccountNumber(accountNumber);
    }

    public AccountResponse findAccountByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(IllegalArgumentException::new);
        return new AccountResponse(
                account.getAccountNumber(),
                account.getBalance(),
                account.getCurrency()
        );

    }

    @Transactional
    public void deleteAccountByAccountNumber(String accountNumber) {
        accountRepository.deleteAccountByAccountNumber(accountNumber);
    }
}
