package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration  // Make sure @Configuration annotation is placed at the top
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/files/upload",
                    "/api/files/download",
                    "/api/files/list",
                    "/login.html",       // Allow access to login page
                    "/index.html",       // Allow access to home page
                    "/upload.html",      // Allow access to upload page
                    "/download.html",    // Allow access to download page
                    "/contact.html",     // Allow access to contact page
                    "/css/**",           // Allow access to CSS files
                    "/js/**"             // Allow access to JS files
                ).permitAll() // <-- Allow all these endpoints without authentication
                .anyRequest().authenticated() // All other requests require authentication
            )
            .httpBasic(); // Basic auth
        return http.build();
    }
}
