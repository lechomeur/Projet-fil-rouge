package com.omadi.projetfilrouge_v2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omadi.projetfilrouge_v2.entities.JwtService;
import com.omadi.projetfilrouge_v2.entities.Utilisateurs;
import com.omadi.projetfilrouge_v2.services.AuthentificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthentificationControllerTest {

    @Mock
    private AuthentificationService authentificationService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthentificationController controller;

    private Utilisateurs mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new Utilisateurs();
        mockUser.setId(1L);
        mockUser.setNom_utilisateur("john");
        mockUser.setMot_de_passe("password");
        mockUser.setEmail("john@example.com");
        mockUser.setRole("ROLE_USER");
    }
    // ---------------- LOGIN ----------------
    @Test
    void testLoginSuccess() {
        when(authentificationService.getByUsername("john")).thenReturn(Optional.of(mockUser));
        when(authentificationService.authentication("john", "password")).thenReturn(Optional.of(mockUser));
        when(jwtService.generateAccessToken("john", "ROLE_USER")).thenReturn("access123");
        when(jwtService.generateRefreshToken("john")).thenReturn("refresh123");

        ResponseEntity<?> response = controller.login(mockUser);

        assertEquals(200, response.getStatusCodeValue());
        Map<String, String> body = (Map<String, String>) response.getBody();
        assertEquals("access123", body.get("access_token"));
        assertEquals("refresh123", body.get("refresh_token"));
    }

    @Test
    void testLoginUserNotFound() {
        when(authentificationService.getByUsername("john")).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.login(mockUser);

        assertEquals(404, response.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Utilisateur inexistant", body.get("error"));
    }

    @Test
    void testLoginInvalidPassword() {
        when(authentificationService.getByUsername("john")).thenReturn(Optional.of(mockUser));
        when(authentificationService.authentication("john", "password")).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.login(mockUser);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Identifiants manquant", response.getBody());
    }

    // ---------------- REGISTER ----------------
    @Test
    void testRegisterSuccess() {
        when(authentificationService.register(any(Utilisateurs.class))).thenReturn(mockUser);
        when(jwtService.generateAccessToken("john", "ROLE_USER")).thenReturn("accessXYZ");
        when(jwtService.generateRefreshToken("john")).thenReturn("refreshXYZ");

        ResponseEntity<?> response = controller.register(mockUser);

        assertEquals(200, response.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("john", body.get("nom_utilisateur"));
        assertEquals("accessXYZ", body.get("access_token"));
    }

    @Test
    void testRegisterConflict() {
        when(authentificationService.register(any(Utilisateurs.class)))
                .thenThrow(new IllegalStateException("Utilisateur déjà existant"));

        ResponseEntity<?> response = controller.register(mockUser);

        assertEquals(409, response.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Utilisateur déjà existant", body.get("error"));
    }

    @Test
    void testRefreshInvalidToken() {
        Map<String, String> request = Map.of("refresh_token", "badToken");
        doThrow(new io.jsonwebtoken.JwtException("TOKEN_INVALID"))
                .when(jwtService).validateToken("badToken", true);

        ResponseEntity<?> response = controller.refresh(request);

        assertEquals(401, response.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("TOKEN_INVALID", body.get("error"));
    }

    // ---------------- LOGOUT ----------------

    @Test
    void testLogoutInvalidToken() {
        doThrow(new io.jsonwebtoken.JwtException("TOKEN_INVALID"))
                .when(jwtService).validateToken("refresh123", true);

        ResponseEntity<?> response = controller.logout(Map.of("refresh_token", "refresh123"));

        assertEquals(401, response.getStatusCodeValue());
    }
}
