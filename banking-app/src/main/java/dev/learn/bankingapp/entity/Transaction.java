package dev.learn.bankingapp.entity;

import dev.learn.bankingapp.constants.TRANSACTION_TYPE;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id", nullable = false)
    private Account fromAccount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id")
    private Account toAccount;
    @Column(nullable = false, scale = 4, precision = 12)
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private TRANSACTION_TYPE type;
    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Transaction(Account fromAccount, Account toAccount, BigDecimal balance, TRANSACTION_TYPE type, LocalDateTime timestamp) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.balance = balance;
        this.type = type;
        this.timestamp = timestamp;
    }

    public Transaction() {
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public TRANSACTION_TYPE getType() {
        return type;
    }

    public void setType(TRANSACTION_TYPE type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static Transaction transfer(Account from, Account to, BigDecimal amount) {
        if (from.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }
        from.withdraw(amount);
        to.deposit(amount);

        return new Transaction(from, to, amount, TRANSACTION_TYPE.TRANSFER, LocalDateTime.now());
    }

}
