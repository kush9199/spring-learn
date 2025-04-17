package com.studenterp.jwtauth.filter;

import com.studenterp.jwtauth.repository.MemberRepository;
import com.studenterp.jwtauth.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final MemberRepository memberRepository;

    public JwtFilter(JWTService jwtService, MemberRepository memberRepository) {
        this.jwtService = jwtService;
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        // authorization header
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            // extract token from header
            String token = authHeader.substring(7);
            // decode token for username
            String username = jwtService
                    .extractUsername(token,(claims)-> claims.getSubject());
            // extract userDetails from username
            UserDetails userDetails = memberRepository
                    .findMemberByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(username));
            // token isValidated ?
            if(jwtService.validateToken(token,userDetails)){
                // UsernamePasswordAuthenticationToken userDetails
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                // token set in SecurityContext
                if(authToken.isAuthenticated()){
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        doFilter(request, response, filterChain);
    }
}
