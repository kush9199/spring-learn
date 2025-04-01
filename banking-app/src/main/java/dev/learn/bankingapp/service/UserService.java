package dev.learn.bankingapp.service;

import dev.learn.bankingapp.dto.UserRequest;
import dev.learn.bankingapp.entity.User;
import dev.learn.bankingapp.reposiotry.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(UserRequest user) {
        return userRepository.save(new User(user.username, user.password, user.email, user.role));
    }

    public boolean findByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Optional<User> getUser(String username) {
        return userRepository.getUserByUsername(username);
    }

    @Transactional
    public void deleteUserByUsername(String username) {
        userRepository.deleteUserByUsername(username);
    }
}
