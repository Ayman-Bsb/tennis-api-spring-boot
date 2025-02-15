package com.ayman.tennis.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// @EnableWebSecurity enables custom security configuration in Spring Security,
// allowing customization of protected paths, authentication methods, roles, permissions, etc.
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private AppUserDetailsService appUserDetailsService;

    @Bean
    // Defines a PasswordEncoder component to encode user passwords during authentication.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(appUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF (Cross-Site Request Forgery) protection
                .headers(headers ->
                        headers
                                // To protect against Cross Site Scripting (XSS) attacks.
                                .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self' data:; style-src 'self' 'unsafe-inline';"))

                                // To protect against Clickjacking attacks.
                                .frameOptions(frameOptionsConfig -> frameOptionsConfig.deny())
                                // To protect privacy.
                                .permissionsPolicy(permissionsPolicyConfig -> permissionsPolicyConfig.policy("fullscreen=(self), geolocation=(), microphone=(), camera=()"))
                )
                .authorizeHttpRequests(authorizations -> // We define rules that will be applied to various endpoints of our API.
                        authorizations
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/accounts/login").permitAll()
                                .requestMatchers("/healthcheck").permitAll()
                                .requestMatchers(HttpMethod.GET,"/players/**").hasAnyAuthority("ROLE_USER")
                                .requestMatchers(HttpMethod.POST, "/players/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/players/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/players/**").hasAuthority("ROLE_ADMIN")
                                .anyRequest().authenticated() // For all other paths, authentication is mandatory.
                );

        return http.build();
    }
}
