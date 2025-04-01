package dev.learn.bankingapp.controller;

import dev.learn.bankingapp.dto.ErrorResponse;
import dev.learn.bankingapp.dto.UserRequest;
import dev.learn.bankingapp.dto.UserResponse;
import dev.learn.bankingapp.entity.User;
import dev.learn.bankingapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest user) {
        if(user.username == null){
            ErrorResponse resp = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "not valid credentials",
                    LocalDateTime.now());
            return ResponseEntity.badRequest().body(resp);
        }
        if(user.email.isEmpty() ||
                user.password.isEmpty() ||
                user.username.isEmpty() ||
                user.role.name().isEmpty()) {
            ErrorResponse resp = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "not valid credentials",
                    LocalDateTime.now());
            return ResponseEntity.badRequest().body(resp);
        }
        if(userService.findByUsername(user.username)){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(HttpStatus.CONFLICT.value(),
                            "user already existed",
                            LocalDateTime.now()));
        }
        User savedUser = userService.registerUser(user);
        UserResponse resp = new UserResponse(savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole().name());
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        if(!userService.findByUsername(username)){
            ErrorResponse resp = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "user not found",
                    LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }
        User u = userService.getUser(username).orElseThrow(IllegalAccessError::new);
        UserResponse resp = new UserResponse(u.getUsername(),
                u.getEmail(),
                u.getRole().name());
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        if(!userService.findByUsername(username)){
            ErrorResponse resp = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "user not found",
                    LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }
        userService.deleteUserByUsername(username);
        return ResponseEntity.noContent().build();
    }
}
