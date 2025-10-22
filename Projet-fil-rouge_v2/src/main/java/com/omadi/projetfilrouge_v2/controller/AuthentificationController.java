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
        System.out.println("✅ [DEBUG] Requête reçue sur /auth/login avec utilisateur : " + request.getNom_utilisateur());
        Optional<Utilisateurs> user = authentificationService.getByUsername(request.getNom_utilisateur());
        if (user.isEmpty()) {
            return ResponseEntity.status(401).body("Identifiants manquant");
        } else if (!user.get().getMot_de_passe().equals(request.getMot_de_passe())) {
            return ResponseEntity.status(401).body("Identifiants incorecte");
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
         
    }
}
