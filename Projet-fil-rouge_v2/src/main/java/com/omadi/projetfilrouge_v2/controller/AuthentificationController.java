package com.omadi.projetfilrouge_v2.controller;

import com.omadi.projetfilrouge_v2.entities.JwtService;
import com.omadi.projetfilrouge_v2.entities.Utilisateurs;
import com.omadi.projetfilrouge_v2.services.AuthentificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("auth")
@CrossOrigin("*")
public  class AuthentificationController {
    private final AuthentificationService authentificationService;
    private JwtService jwtService;
    private final Map<String, String> validRefreshTokens = new HashMap<>();
    public AuthentificationController(AuthentificationService authentificationService,
                                      JwtService jwtService) {
        this.authentificationService = authentificationService;
        this.jwtService = jwtService;
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Utilisateurs request) {
        Optional<Utilisateurs> user = authentificationService.authentication(request.getNom_utilisateur(), request.getMot_de_passe());
        Optional<Utilisateurs> userExist = authentificationService.getByUsername(request.getNom_utilisateur());
        if (userExist.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Utilisateur inexistant"));
        }
        if (user.isEmpty()) {
            return ResponseEntity.status(401).body("Identifiants manquant");
        }
        String username = user.get().getNom_utilisateur();
        String role = user.get().getRole();
        try {
            String accessToken = jwtService.generateAccessToken(username, role);
            String refreshToken = jwtService.generateRefreshToken(username);
            validRefreshTokens.put(username, refreshToken);

            return ResponseEntity.ok(Map.of(
                    "access_token", accessToken,
                    "refresh_token", refreshToken
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur interne lors de la génération du token");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Utilisateurs request) {
        try {
            Utilisateurs userCreat = authentificationService.register(request);
            String username = userCreat.getNom_utilisateur();
            String role = userCreat.getRole();
            String accessToken = jwtService.generateAccessToken(username, role);
            String refreshToken = jwtService.generateRefreshToken(username);
            validRefreshTokens.put(username, refreshToken);
            Map<String, Object> response = Map.of(
                    "id", userCreat.getId(),
                    "nom_utilisateur", userCreat.getNom_utilisateur(),
                    "email", userCreat.getEmail(),
                    "role", userCreat.getRole(),
                    "access_token", accessToken,
                    "refresh_token", refreshToken
            );
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public  ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {
        try {
            String refreshToken = body.get("refresh_token");
            if (refreshToken == null || refreshToken.isEmpty()) {
                return ResponseEntity.status(400).body(Map.of("error", "Le refresh token est manquant"));
            }
            jwtService.validateToken(refreshToken, true);
            String username = jwtService.extractUsername(refreshToken, true);

            String validToken = validRefreshTokens.get(username);
            if (validToken == null || !validToken.equals(refreshToken)) {
                return ResponseEntity.status(401).body(Map.of("error", "Refresh token invalide ou expiré"));
            }
            Optional<Utilisateurs> user = authentificationService.getByUsername(username);
            if (user.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "Utilisateur introuvable"));
            }
            String newAccessToken = jwtService.generateAccessToken(username, user.get().getRole());
            return ResponseEntity.ok(Map.of(
                    "access_token", newAccessToken,
                    "refresh_token", refreshToken
            ));
        } catch (io.jsonwebtoken.JwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Erreur interne lors du refresh"));
        }
    }
}


