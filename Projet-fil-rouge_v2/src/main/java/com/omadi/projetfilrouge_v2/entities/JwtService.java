package com.omadi.projetfilrouge_v2.entities;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final String accessSecret;
    private final String refreshSecret;
    private final long accessExpiration;
    private final long refreshExpiration;

    public JwtService(Dotenv dotenv) {
        String access = dotenv.get("JWT_ACCESS_SECRET");
        String refresh = dotenv.get("JWT_REFRESH_SECRET");

        if (access == null || refresh == null) {
            throw new IllegalStateException("❌ Les variables JWT_* ne sont pas chargées depuis le fichier .env");
        }

        this.accessSecret = dotenv.get("JWT_ACCESS_SECRET");
        this.refreshSecret =dotenv.get("JWT_REFRESH_SECRET");
        this.accessExpiration = Long.parseLong(dotenv.get("JWT_ACCESS_EXPIRATION"));
        this.refreshExpiration = Long.parseLong(dotenv.get("JWT_REFRESH_EXPIRATION"));
    }

    public String generateAccessToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claims(Map.of("role", role))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)

                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)

                .compact();
    }
    public boolean validateToken(String token, boolean isRefresh) {
        try {
            Jwts.parser()
                    .setSigningKey(isRefresh ? refreshSecret : accessSecret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
    public String extractUsername(String token, boolean isRefresh) {
        return Jwts.parser()
                .setSigningKey(isRefresh ? refreshSecret : accessSecret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
