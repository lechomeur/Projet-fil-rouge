package com.omadi.projetfilrouge_v2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable()) // désactivation CSRF pour tests API
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/admin/**").hasRole("ADMIN")
                            .requestMatchers("/tresorier/**").hasAnyRole("ADMIN", "TRESORIER")
                            .requestMatchers("/user/**").hasAnyRole("ADMIN", "TRESORIER", "ADHERENT")
                            .requestMatchers("/public/**", "/h2-console/**", "/auth/**").permitAll()
                            .anyRequest().authenticated()
                    )
                    .formLogin(form -> form.disable())
                    .httpBasic(httpBasic -> httpBasic.disable())
                    .headers(headers -> headers.frameOptions(frame -> frame.disable()));
            return http.build();
        }
    }


