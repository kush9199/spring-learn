package dev.learn.bankingapp.dto;

import java.time.LocalDateTime;

public class ErrorResponse {
    public int status;
    public String message;
    public LocalDateTime timestamp;
    public ErrorResponse(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }
}
