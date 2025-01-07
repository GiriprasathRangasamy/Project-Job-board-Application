package com.example.projectspring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disable CSRF for development; enable properly for production
            .authorizeHttpRequests()
            .requestMatchers("/api1/register", "/api1/login","/user/**","/recruiter/**","/company/**,/api/payment/**").permitAll() // Allow access to /register and /login without authentication
            .anyRequest().permitAll() // Require authentication for any other requests
            .and()
            .formLogin().disable(); // Disable default form-based login

        return http.build();
    }

    // Password encoder for encrypting passwords
   
}
