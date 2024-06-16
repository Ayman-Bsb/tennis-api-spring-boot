package com.ayman.tennis.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

// This configuration overrides the default settings
// all security rules are ignored for all requests, specifically for the 'test' profile.
@Configuration
@Profile("test")
public class ApplicationWithoutSecurity {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().anyRequest();
    }
}
