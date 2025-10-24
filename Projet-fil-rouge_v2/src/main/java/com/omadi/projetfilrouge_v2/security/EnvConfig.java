package com.omadi.projetfilrouge_v2.security;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ⚙️ Configuration Spring pour charger le fichier .env dans l'application.
 *
 * ✅ Objectif :
 *  - Fournir un bean {@link Dotenv} à l’ensemble du projet Spring.
 *  - Faciliter l’accès aux variables d’environnement définies dans le fichier `.env`
 *    (comme les clés JWT, les mots de passe, les URL de BDD, etc.).
 */
@Configuration
public class EnvConfig {

    @Bean
    public Dotenv dotenv() {
        return Dotenv.load();
    }
}