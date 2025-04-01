package dev.learn.virgin_security.controller;

import dev.learn.virgin_security.entity.Member;
import dev.learn.virgin_security.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth/")
public class AuthController {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthController(
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody Member user) {
        if(memberRepository.findByUsername(user.getUsername()).isEmpty()) {
            Member member = new Member();
            member.setUsername(user.getUsername());
            member.setPassword(passwordEncoder.encode(user.getPassword()));
            member.setFirstName(user.getFirstName());
            member.setLastName(user.getLastName());
            return ResponseEntity.ok(memberRepository.save(member));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @GetMapping("login")
    public ResponseEntity<?> login(@RequestBody Member user) {
        boolean isLogin = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getUsername(),
                                user.getPassword()))
                .isAuthenticated();
        return isLogin
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
