package com.omadi.projetfilrouge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // désactive CSRF pour les tests API (optionnel selon ton usage)
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll() // autorise toutes les requêtes
                )
                .formLogin().disable()      // désactive la page de login form
                .httpBasic().disable();     // désactive http basic

        return http.build();
    }
}
