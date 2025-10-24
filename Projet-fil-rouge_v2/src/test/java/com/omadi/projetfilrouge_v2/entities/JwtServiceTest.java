package com.omadi.projetfilrouge_v2.entities;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static javax.swing.UIManager.put;
import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {
    private JwtService jwtService;

    @BeforeEach
    void setup() {
        Dotenv dotenv = new MyDotenv();
        jwtService = new JwtService(dotenv);
    }

    @Test
    void testGenerateAccessToken_shouldContainUsernameAndRole() {
        String token = jwtService.generateAccessToken("user1", "ROLE_ADMIN");

        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3);

        String username = jwtService.extractUsername(token, false);
        String role = jwtService.extractRole(token);

        assertEquals("user1", username);
        assertEquals("ROLE_ADMIN", role);
    }

    @Test
    void testGenerateRefreshToken_shouldBeValid() {
        String token = jwtService.generateRefreshToken("refreshUser");

        assertNotNull(token);
        jwtService.validateToken(token, true); // ne doit pas lever d’exception
    }

    @Test
    void testValidateToken_withInvalidSignature_shouldThrow() {
        String validToken = jwtService.generateAccessToken("user2", "ROLE_USER");


        String invalidToken = validToken.substring(0, validToken.length() - 2) + "xx";

        assertThrows(JwtException.class, () -> jwtService.validateToken(invalidToken, false));
    }

    @Test
    void testExtractUsername_shouldReturnCorrectValue() {
        String token = jwtService.generateAccessToken("user3", "ROLE_MANAGER");
        String username = jwtService.extractUsername(token, false);

        assertEquals("user3", username);
    }

    @Test
    void testGenerateAccessToken_withEmptyRole_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> jwtService.generateAccessToken("user4", ""));
    }

    @Test
    void testValidateToken_withValidToken_shouldNotThrow() {
        String token = jwtService.generateAccessToken("user5", "ROLE_USER");

        assertDoesNotThrow(() -> jwtService.validateToken(token, false));
    }

    @Test
    void testExpiredToken_shouldThrow() throws InterruptedException {
        // Expiration courte : 100 ms
        Dotenv shortLivedEnv = new Dotenv() {
            @Override
            public Set<DotenvEntry> entries() {
                return Set.of();
            }

            @Override
            public Set<DotenvEntry> entries(Filter filter) {
                return Set.of();
            }

            @Override
            public String get(String key) {
                return switch (key) {
                    case "JWT_ACCESS_SECRET" -> "12345678901234567890123456789012";
                    case "JWT_REFRESH_SECRET" -> "abcdefghijklmnopqrstuvwxzy1234567890";
                    case "JWT_ACCESS_EXPIRATION" -> "100";
                    case "JWT_REFRESH_EXPIRATION" -> "100";
                    default -> null;
                };
            }

            @Override
            public String get(String s, String s1) {
                return "";
            }
        };

        JwtService shortLivedService = new JwtService(shortLivedEnv);
        String token = shortLivedService.generateAccessToken("expiredUser", "ROLE_USER");

        Thread.sleep(200); // attendre pour expiration

        assertThrows(JwtException.class, () -> shortLivedService.validateToken(token, false));
    }

    private static class MyDotenv implements Dotenv {
        private final Map<String, String> vars = new HashMap<>() {{
            put("JWT_ACCESS_SECRET", "12345678901234567890123456789012"); // 32+ caractères
            put("JWT_REFRESH_SECRET", "abcdefghijklmnopqrstuvwxzy1234567890");
            put("JWT_ACCESS_EXPIRATION", "60000");   // 1 minute
            put("JWT_REFRESH_EXPIRATION", "3600000"); // 1 heure
        }};

        @Override
        public Set<DotenvEntry> entries() {
            return Set.of();
        }

        @Override
        public Set<DotenvEntry> entries(Filter filter) {
            return Set.of();
        }

        @Override
        public String get(String key) {
            return vars.get(key);
        }

        @Override
        public String get(String s, String s1) {
            return "";
        }
    }
}
