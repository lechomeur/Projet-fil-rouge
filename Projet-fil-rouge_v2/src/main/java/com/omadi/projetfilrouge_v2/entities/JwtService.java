package com.omadi.projetfilrouge_v2.entities;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final Key accessKey;
    private final Key refreshKey;
    private final long accessExpiration;
    private final long refreshExpiration;

    public JwtService(Dotenv dotenv) {
        String access = dotenv.get("JWT_ACCESS_SECRET");
        String refresh = dotenv.get("JWT_REFRESH_SECRET");

        if (access == null || refresh == null) {
            throw new IllegalStateException(" pas chargées depuis le fichier .env");
        }
        this.accessKey = Keys.hmacShaKeyFor(access.getBytes(StandardCharsets.UTF_8));
        this.refreshKey = Keys.hmacShaKeyFor(refresh.getBytes(StandardCharsets.UTF_8));
        this.accessExpiration = Long.parseLong(dotenv.get("JWT_ACCESS_EXPIRATION"));
        this.refreshExpiration = Long.parseLong(dotenv.get("JWT_REFRESH_EXPIRATION"));
    }

    public String generateAccessToken(String username, String role) {

        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("❌ Le rôle ne peut pas être nul ou vide lors de la génération du token !");
        }
        String token =
          Jwts.builder()
                .subject(username)
                .claims(Map.of("role", role))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(accessKey)
                .compact();
        System.out.println(" Token généré pour l'utilisateur : " + username);
        System.out.println("Rôle ajouté dans le token : " + role);
        return token;
    }
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(refreshKey)
                .compact();
    }

    public void validateToken(String token, boolean isRefresh) {
        try {
            Key key = isRefresh ? refreshKey : accessKey;

            Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token);

        } catch (ExpiredJwtException e) {
            throw new JwtException("TOKEN_EXPIRED");
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new JwtException("TOKEN_INVALID");
        } catch (SecurityException e) {
            throw new JwtException("INVALID_SIGNATURE");
        }
    }

    public String extractUsername(String token, boolean isRefresh) {
        Key key = isRefresh ? refreshKey : accessKey;

        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    public String extractRole(String token) {
        return (String) Jwts.parser()
                .verifyWith((SecretKey) accessKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role");
    }
}
