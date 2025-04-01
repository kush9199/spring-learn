package dev.learn.bankingapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learn.bankingapp.constants.ACCOUNT_TYPE;
import dev.learn.bankingapp.constants.ROLE;
import dev.learn.bankingapp.entity.Account;
import dev.learn.bankingapp.entity.User;
import dev.learn.bankingapp.reposiotry.AccountRepository;
import dev.learn.bankingapp.reposiotry.TransactionRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ObjectMapper om;

    private Account senderAccount;
    private Account recieverAccount;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();

        User sendUser = new User(
                "senderUser",
                "password",
                "sendUser@gmail.com",
                ROLE.CUSTOMER);
        User savedSenderUser = userRepository.save(sendUser);

        User receiverUser = new User(
                "recieverUser",
                "password",
                "reciverUser@gmail.com",
                ROLE.CUSTOMER
        );
        User savedReceiverUser = userRepository.save(receiverUser);

        senderAccount = new Account();
        senderAccount.setUser(savedSenderUser);
        senderAccount.setType(ACCOUNT_TYPE.SAVING);
        senderAccount.setBalance(new BigDecimal("3000.00"));
        senderAccount.setCurrency("INR");
        senderAccount.setAccountNumber(
                accountService.generateAccountNumber(
                        savedSenderUser.getUsername(), LocalDateTime.now()
                )
        );
        senderAccount = accountRepository.save(senderAccount);
        savedSenderUser.getAccounts().add(senderAccount);
        userRepository.save(savedSenderUser);

        recieverAccount = new Account();
        recieverAccount.setUser(receiverUser);
        recieverAccount.setType(ACCOUNT_TYPE.SAVING);
        recieverAccount.setBalance(new BigDecimal("1000.00"));
        recieverAccount.setCurrency("INR");
        recieverAccount.setAccountNumber(
                accountService.generateAccountNumber(
                        receiverUser.getUsername(), LocalDateTime.now()
                )
        );
        recieverAccount = accountRepository.save(recieverAccount);
        sendUser.getAccounts().add(recieverAccount);
        userRepository.save(receiverUser);
    }

    @Test
    @Order(15)
    void shouldDepositSuccessfully() throws Exception {
        HashMap<String, Object> body = new HashMap<>();
        body.put("accountNumber", senderAccount.getAccountNumber());
        body.put("fromAccount", "");
        body.put("toAccount", "");
        body.put("amount", 5000.00);

        mvc.perform(post("/api/v1/transactions/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(16)
    void shouldReturnNotFoundForNonExistentAccountOnDeposit() throws Exception {
        HashMap<String, Object> body = new HashMap<>();
        body.put("accountNumber", "0000000000000000");
        body.put("fromAccount", "");
        body.put("toAccount", "");
        body.put("amount", 5000.00);

        mvc.perform(post("/api/v1/transactions/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(17)
    void shouldWithdrawSuccessfully() throws Exception {
        HashMap<String, Object> body = new HashMap<>();
        body.put("accountNumber", senderAccount.getAccountNumber());
        body.put("fromAccount", "");
        body.put("toAccount", "");
        body.put("amount", 3000.00);

        mvc.perform(post("/api/v1/transactions/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(18)
    void shouldReturnBadRequestForInsufficientFunds() throws Exception {
        HashMap<String, Object> body = new HashMap<>();
        body.put("accountNumber", senderAccount.getAccountNumber());
        body.put("fromAccount", "");
        body.put("toAccount", "");
        body.put("amount", 112000.00);

        mvc.perform(post("/api/v1/transactions/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(19)
    void shouldTransferSuccessfully() throws Exception {
        HashMap<String, Object> body = new HashMap<>();
        body.put("accountNumber", "");
        body.put("fromAccount", senderAccount.getAccountNumber());
        body.put("toAccount", recieverAccount.getAccountNumber());
        body.put("amount", 2000.00);

        mvc.perform(post("/api/v1/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(20)
    void shouldReturnBadRequestForTransferToSameAccount() throws Exception {
        HashMap<String, Object> body = new HashMap<>();
        body.put("accountNumber", "");
        body.put("fromAccount", senderAccount.getAccountNumber());
        body.put("toAccount", senderAccount.getAccountNumber());
        body.put("amount", 200.00);

        mvc.perform(post("/api/v1/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }
}
