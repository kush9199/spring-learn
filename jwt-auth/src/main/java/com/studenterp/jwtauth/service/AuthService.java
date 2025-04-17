package com.studenterp.jwtauth.service;

import com.studenterp.jwtauth.constant.ROLE;
import com.studenterp.jwtauth.dto.RegisterRequest;
import com.studenterp.jwtauth.entity.Member;
import com.studenterp.jwtauth.repository.MemberRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       UserDetailsService userDetailsService,
                       JWTService jwtService,
                       PasswordEncoder passwordEncoder,
                       MemberRepository memberRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
    }


    public boolean isUserPresent(String username) {
        return memberRepository.existsMemberByUsername(username);
    }

    public void createUser(RegisterRequest reg) {
        Member member = new Member();
        member.setUsername(reg.username);
        member.setPassword(passwordEncoder.encode(reg.password));
        member.setRole(ROLE.ROLE_USER);
        memberRepository.save(member);
    }

    public String authenticateUser(RegisterRequest reg) {
        boolean isAuthenticated = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        reg.username, reg.password
                )).isAuthenticated();
        if (isAuthenticated) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(reg.username);
            return jwtService.generateToken(userDetails);
        }
        throw new UsernameNotFoundException("unable to login");
    }
}
