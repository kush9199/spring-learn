package dev.learn.bankingapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learn.bankingapp.constants.ACCOUNT_TYPE;
import dev.learn.bankingapp.constants.ROLE;
import dev.learn.bankingapp.entity.Account;
import dev.learn.bankingapp.entity.User;
import dev.learn.bankingapp.reposiotry.AccountRepository;
import dev.learn.bankingapp.reposiotry.UserRepository;
import dev.learn.bankingapp.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper om;

    private User user;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        userRepository.deleteAll();

        user = new User(
                "userAccount",
                "password",
                "useraccount@gmail.com",
                ROLE.CUSTOMER
        );
        userRepository.save(user);
    }

    @Test
    @Order(9)
    void shouldCreateAccountSuccessfully() throws Exception {
        HashMap<String, Object> body = new HashMap<>();
        body.put("username", "userAccount");
        body.put("currency", "INR");
        body.put("accountType", ACCOUNT_TYPE.SAVING);

        mvc.perform(post("/api/v1/accounts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(10)
    void shouldReturnBadRequestForMissingBody() throws Exception {
        mvc.perform(post("/api/v1/accounts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(11)
    void shouldReturnConflictForDuplicateAccount() throws Exception {
        Account account = new Account();
        account.setUser(user);
        account.setType(ACCOUNT_TYPE.SAVING);
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency("INR");
        account.setAccountNumber(accountService.generateAccountNumber(user.getUsername(), LocalDateTime.now()));
        accountRepository.save(account);
        user.getAccounts().add(account);
        userRepository.save(user);

        HashMap<String, Object> body = new HashMap<>();
        body.put("username", "userAccount");
        body.put("currency", "INR");
        body.put("accountType", ACCOUNT_TYPE.SAVING);

        mvc.perform(post("/api/v1/accounts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(12)
    void shouldRetrieveAccountSuccessfully() throws Exception {
        Account account = new Account();
        account.setUser(user);
        account.setType(ACCOUNT_TYPE.SAVING);
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency("INR");
        account.setAccountNumber(accountService.generateAccountNumber(user.getUsername(), LocalDateTime.now()));
        accountRepository.save(account);
        user.getAccounts().add(account);
        userRepository.save(user);

        mvc.perform(get("/api/v1/accounts/" + account.getAccountNumber()))
                .andExpect(status().isOk());
    }

    @Test
    @Order(13)
    void shouldReturnNotFoundForNonExistentAccount() throws Exception {
        mvc.perform(get("/api/v1/accounts/00000000"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(14)
    void shouldDeleteAccountSuccessfully() throws Exception {
        Account account = new Account();
        account.setUser(user);
        account.setType(ACCOUNT_TYPE.SAVING);
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency("INR");
        account.setAccountNumber(accountService.generateAccountNumber(user.getUsername(), LocalDateTime.now()));
        Account savedAccount = accountRepository.save(account);
        user.getAccounts().add(account);
        userRepository.save(user);

        mvc.perform(delete("/api/v1/accounts/" + savedAccount.getAccountNumber().strip()))
                .andExpect(status().isNoContent());
    }

}
