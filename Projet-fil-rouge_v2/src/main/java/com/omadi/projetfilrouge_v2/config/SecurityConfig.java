package com.omadi.projetfilrouge_v2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 🔐 Configuration principale de la sécurité Spring.
 *
 * ✅ Objectif :
 *  - Gérer les accès aux différentes routes HTTP.
 *  - Ajouter un filtre JWT personnalisé avant le filtre d’authentification standard.
 *  - Désactiver les mécanismes non utilisés (CSRF, login form, HTTP Basic...).
 */
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Injection du filtre JWT personnalisé.
     * Ce filtre valide les tokens et authentifie les utilisateurs sur chaque requête.
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Déclare le bean SecurityFilterChain, qui définit toute la configuration de sécurité HTTP.
     *
     * @param http l’objet HttpSecurity de Spring Security
     * @return la chaîne de filtres configurée
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/public/**", "/h2-console/**","/swagger/**").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/tresorier/**").hasAnyRole("ADMIN", "TRESORIER")
                        .requestMatchers("/user/**").hasAnyRole("ADMIN", "TRESORIER", "ADHERENT")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));
        return http.build();
    }
}
