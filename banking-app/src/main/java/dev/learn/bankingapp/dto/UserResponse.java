package dev.learn.bankingapp.dto;

public class UserResponse {
    public String username;
    public String email;
    public String role;

    public UserResponse(String username, String email, String role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
