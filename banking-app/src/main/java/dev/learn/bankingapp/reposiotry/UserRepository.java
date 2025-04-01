package dev.learn.bankingapp.reposiotry;

import dev.learn.bankingapp.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> getUserByUsername(String username);

    @Transactional
    void deleteUserByUsername(String username);

    Optional<User> findByUsername(String username);
}
