package dev.learn.bankingapp.reposiotry;

import dev.learn.bankingapp.entity.Account;
import dev.learn.bankingapp.entity.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class TransferRepository {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Transaction transferMoney(Transaction transaction) {
        em.lock(transaction.getFromAccount(), LockModeType.PESSIMISTIC_WRITE);
        em.lock(transaction.getToAccount(), LockModeType.PESSIMISTIC_WRITE);
        em.persist(transaction);
        return transaction;
    }
}
