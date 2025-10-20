package com.omadi.projetfilrouge.controller;


import com.omadi.projetfilrouge.entities.Utilisateurs;
import com.omadi.projetfilrouge.services.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UtilisateurController {
    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<Utilisateurs> users = utilisateurService.list();
        if (users.isEmpty()) {
            return new ResponseEntity<>("Aucun utilisateur trouvé", HttpStatus.NO_CONTENT); // 204
        } else {
            return new ResponseEntity<>(users, HttpStatus.OK); // 200
        }
    }
}
