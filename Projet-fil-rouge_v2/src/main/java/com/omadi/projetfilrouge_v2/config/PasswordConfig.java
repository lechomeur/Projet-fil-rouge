package com.omadi.projetfilrouge_v2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 🧩 Configuration de l'encodeur de mots de passe pour Spring Security.
 *
 * ✅ Objectif :
 *  - Définir un bean unique de type {@link PasswordEncoder}.
 *  - Utiliser l'algorithme BCrypt pour hacher les mots de passe des utilisateurs.
 */
@Configuration
public class PasswordConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
