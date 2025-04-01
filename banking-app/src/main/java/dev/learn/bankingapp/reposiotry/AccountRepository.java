package dev.learn.bankingapp.reposiotry;

import dev.learn.bankingapp.entity.Account;

import dev.learn.bankingapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> user(User user);

    boolean existsAccountByUser_Username(String username);

    boolean existsAccountByAccountNumber(String accountNumber);

    @Transactional
    void deleteAccountByAccountNumber(String accountNumber);
}
