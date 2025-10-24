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

    /**
     * Constructeur du service JWT.
     * Il charge les clés secrètes et durées d'expiration depuis le fichier `.env`.
     * Ces valeurs sont utilisées pour signer et valider les tokens.
     */
    public JwtService(Dotenv dotenv) {
        String access = dotenv.get("JWT_ACCESS_SECRET");
        String refresh = dotenv.get("JWT_REFRESH_SECRET");

        if (access == null || refresh == null) {
            throw new IllegalStateException(" pas chargées depuis le fichier .env");
        }
        // Conversion des secrets en clés HMAC (hachées pour la sécurité)
        this.accessKey = Keys.hmacShaKeyFor(access.getBytes(StandardCharsets.UTF_8));
        this.refreshKey = Keys.hmacShaKeyFor(refresh.getBytes(StandardCharsets.UTF_8));

        this.accessExpiration = Long.parseLong(dotenv.get("JWT_ACCESS_EXPIRATION"));
        this.refreshExpiration = Long.parseLong(dotenv.get("JWT_REFRESH_EXPIRATION"));
    }

    /**
     * Génère un token d'accès JWT (court terme)  pour un utilisateur.
     *
     * @param username le nom d'utilisateur
     * @param role     le rôle associé à l'utilisateur (admin, user, etc.)
     * @return un token JWT signé contenant le nom et le rôle
     */
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
    /**
     * Génère un token de rafraîchissement (long terme).
     * Utilisé pour obtenir un nouveau token d’accès sans se reconnecter.
     *
     * @param username le nom d'utilisateur
     * @return un token JWT de rafraîchissement signé
     */
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(refreshKey)
                .compact();
    }
    /**
     * Valide un token JWT (d'accès ou de rafraîchissement).
     * Lève une exception si le token est expiré, invalide ou mal signé.
     *
     * @param token     le token à valider
     * @param isRefresh vrai si c'est un token de rafraîchissement
     */
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
    /**
     * Extrait le nom d'utilisateur contenu dans un token JWT.
     *
     * @param token     le token à analyser
     * @param isRefresh vrai si le token est un refresh token
     * @return le nom d'utilisateur contenu dans le token
     */
    public String extractUsername(String token, boolean isRefresh) {
        Key key = isRefresh ? refreshKey : accessKey;

        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    /**
     * Extrait le rôle de l'utilisateur depuis un token d'accès.
     *
     * @param token le token d’accès JWT
     * @return le rôle de l’utilisateur (ex: "ROLE_ADMIN")
     */
    public String extractRole(String token) {
        return (String) Jwts.parser()
                .verifyWith((SecretKey) accessKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role");
    }
}
