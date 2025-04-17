package dev.learn.basicauthsecurity.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class BasicSecurityFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        // "Authorization" : "Basic a3VzaGFncmE6cGFzc3dvcmQ="
        String header = request.getHeader("Authorization");
        // a3VzaGFncmE6cGFzc3dvcmQ=
        String token = header.substring(6);
        // username:password
        String credentials = new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
        // username
        String username = credentials.split(":")[0];
        // password
        String password = credentials.split(":")[1];

        UserDetails userDetails = User.withUsername(username).password(password).build();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        System.out.println(username);
        System.out.println(password);
        if(authentication.isAuthenticated()){
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }else{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        doFilter(request, response, filterChain);
    }
}
