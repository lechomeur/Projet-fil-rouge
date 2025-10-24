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
 * ✅ Rôle :
 *  - Intercepter chaque requête HTTP entrante.
 *  - Vérifier la présence d’un en-tête Authorization avec un token "Bearer".
 *  - Extraire et valider le JWT via le JwtService.
 *  - Si le token est valide → authentifier l’utilisateur dans le contexte de sécurité.
 *  - Sinon → renvoyer une erreur HTTP 401 (non autorisé).
 *
 * 📌 Étend OncePerRequestFilter : garantit que le filtre ne s’exécute qu’une seule fois par requête.
 */

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    /**
     * Méthode principale du filtre — exécutée à chaque requête.
     *
     * @param request  La requête HTTP entrante
     * @param response La réponse HTTP à envoyer
     * @param filterChain La chaîne de filtres Spring Security
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = header.substring(7);
        try {
                String username = jwtService.extractUsername(token, false);
                String role = jwtService.extractRole(token);
                var auth = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        List.of(new SimpleGrantedAuthority( role))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
