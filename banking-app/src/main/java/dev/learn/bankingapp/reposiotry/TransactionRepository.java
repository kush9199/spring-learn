package dev.learn.bankingapp.reposiotry;

import dev.learn.bankingapp.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
