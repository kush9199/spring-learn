package com.studenterp.jwtauth.controller.common;

import com.studenterp.jwtauth.dto.RegisterRequest;
import com.studenterp.jwtauth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth/")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest reg) {
        if(reg.username == null){
            return ResponseEntity.badRequest().build();
        }
        if(reg.username.isEmpty() || reg.password.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        if(authService.isUserPresent(reg.username)){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        authService.createUser(reg);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("login")
    public ResponseEntity<?> loginUser(@RequestBody RegisterRequest reg) {
        if(reg.username == null){
            return ResponseEntity.badRequest().build();
        }
        if(reg.username.isEmpty() || reg.password.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        if(!authService.isUserPresent(reg.username)){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        String token = authService.authenticateUser(reg);
        return ResponseEntity.ok().body(token);
    }
}
