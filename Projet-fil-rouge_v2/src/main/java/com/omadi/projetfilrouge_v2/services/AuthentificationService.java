package com.omadi.projetfilrouge_v2.services;

import com.omadi.projetfilrouge_v2.entities.Utilisateurs;
import com.omadi.projetfilrouge_v2.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;

@Service
public class AuthentificationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AuthentificationService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }
    public Optional<Utilisateurs> getByUsername(String nom_utilisateur) {
        return Optional.ofNullable(repository.findByNomUtilisateur(nom_utilisateur));
    }

    public Utilisateurs register(Utilisateurs user) {
        if (repository.findByNomUtilisateur(user.getNom_utilisateur()) != null) {
            throw new IllegalStateException("Nom d'utilisateur déjà utilisé");
        }
        String mdp = user.getMot_de_passe();
        String hash_mdp = passwordEncoder.encode(mdp);
        user.setMot_de_passe(hash_mdp);
        if (user.getRole() == null) user.setRole("ADHERENT");
        return repository.save(user);
    }
    public Optional<Utilisateurs> authentication(String nomUtilisateur, String Password) {
        Utilisateurs user = repository.findByNomUtilisateur(nomUtilisateur);
        if (user == null) return Optional.empty();
        if (passwordEncoder.matches(Password, user.getMot_de_passe())) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }
}
