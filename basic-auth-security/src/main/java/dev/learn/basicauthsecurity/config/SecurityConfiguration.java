package dev.learn.basicauthsecurity.config;

import dev.learn.basicauthsecurity.constants.ROLE;
import dev.learn.basicauthsecurity.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfiguration {
    private final MemberRepository memberRepository;
    private final BasicSecurityFilter basicSecurityFilter;

    public SecurityConfiguration(MemberRepository memberRepository,
                                 BasicSecurityFilter basicSecurityFilter) {
        this.memberRepository = memberRepository;
        this.basicSecurityFilter = basicSecurityFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return memberRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException(username));
            }
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authconfig) throws Exception {
        return authconfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(config-> config.disable())
                .cors(config -> config.disable())
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers("/api/v1/auth").permitAll();
                    requests.requestMatchers("/api/v1/portal").hasAnyRole(ROLE.USER.name());
                    requests.anyRequest().authenticated();
                })
                .sessionManagement(
                        session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .httpBasic(Customizer.withDefaults())
                .addFilterAfter(basicSecurityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
