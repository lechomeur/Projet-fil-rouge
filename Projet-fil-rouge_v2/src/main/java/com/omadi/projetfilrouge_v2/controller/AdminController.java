package com.omadi.projetfilrouge_v2.controller;


import com.omadi.projetfilrouge_v2.entities.Utilisateurs;
import com.omadi.projetfilrouge_v2.services.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("admin")
public class AdminController {
    private final UtilisateurService utilisateurService;

    public AdminController(UtilisateurService utilisateurService) {
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
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<Utilisateurs> user = utilisateurService.get(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<?> getByUsernam(@PathVariable String username) {
        Optional<Utilisateurs> user = utilisateurService.getByUsername(username);
        if (user.isEmpty()) {
            return new ResponseEntity<>("Aucun utilisateur trouvé", HttpStatus.NO_CONTENT); // 204
        } else {
            return new ResponseEntity<>(user, HttpStatus.OK); // 200
        }
    }
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        utilisateurService.delete(id);
        if(ResponseEntity.noContent().build().equals(HttpStatus.NO_CONTENT)) {
            return new ResponseEntity<>("suppression echoué",HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>("utilisateur"+" "+id+" "+"supprimé",HttpStatus.OK);
        }
    }
    @PostMapping()
    public ResponseEntity<?> addUser(@RequestBody Utilisateurs utilisateur) {
        utilisateurService.save(utilisateur);
        if(ResponseEntity.noContent().build().equals(HttpStatus.NO_CONTENT)) {
            return new ResponseEntity<>("suppression echoué",HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>("utilisateur ajouté",HttpStatus.OK);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody Utilisateurs utilisateurUpdate) {
        boolean updated = utilisateurService.updateUtilisateur(id, utilisateurUpdate);
        if (updated) {
            return ResponseEntity.ok("Utilisateur mis à jour avec succès ");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Utilisateur introuvable ");
        }
    }
}
