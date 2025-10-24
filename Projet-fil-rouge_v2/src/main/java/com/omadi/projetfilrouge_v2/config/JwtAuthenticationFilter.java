package com.omadi.projetfilrouge_v2.config;

import com.omadi.projetfilrouge_v2.entities.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * 🔐 Filtre d’authentification JWT pour Spring Security.
 *
 * Intercepte chaque requête HTTP, valide le token JWT et authentifie
 * l'utilisateur s'il est valide.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // 🔓 Étape 1 : ignorer les routes publiques
        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔐 Étape 2 : vérifier l'en-tête Authorization
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔑 Étape 3 : extraire et valider le token JWT
        String token = header.substring(7);
        try {
            String username = jwtService.extractUsername(token, false);
            String role = jwtService.extractRole(token);

            var auth = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    List.of(new SimpleGrantedAuthority(role))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
            return;
        }

        // 🔄 Étape 4 : continuer la chaîne
        filterChain.doFilter(request, response);
    }

    /**
     * Vérifie si la requête correspond à une route publique
     */
    private boolean isPublicPath(String path) {
        return path.startsWith("/auth/")
                || path.startsWith("/public/")
                || path.startsWith("/swagger/")
                || path.startsWith("/h2-console/")
                || path.equals("/")
                || path.equals("/swagger.yaml");
    }
}
