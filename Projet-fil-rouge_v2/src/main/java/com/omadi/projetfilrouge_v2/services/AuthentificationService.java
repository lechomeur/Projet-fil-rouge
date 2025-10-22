package com.omadi.projetfilrouge_v2.services;

import com.omadi.projetfilrouge_v2.entities.Utilisateurs;
import com.omadi.projetfilrouge_v2.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthentificationService {
    private final UserRepository repository;

    public AuthentificationService(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<Utilisateurs> getByUsername(String nom_utilisateur) {
        return Optional.ofNullable(repository.findByNomUtilisateur(nom_utilisateur));
    }
}
