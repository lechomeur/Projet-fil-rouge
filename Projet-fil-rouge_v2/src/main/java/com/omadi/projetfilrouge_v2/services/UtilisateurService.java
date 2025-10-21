package com.omadi.projetfilrouge_v2.services;

import com.omadi.projetfilrouge_v2.entities.Utilisateurs;
import com.omadi.projetfilrouge_v2.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    private final UserRepository repository;

    public UtilisateurService(UserRepository repository) {
        this.repository = repository;
    }

    public List<Utilisateurs> list() {
        return repository.findAll();
    }

    public Optional<Utilisateurs> get(Long id) {
        return repository.findById(id);
    }

    public Optional<Utilisateurs> getByUsername(String nom_utilisateur) {
        return Optional.ofNullable(repository.findByNomUtilisateur(nom_utilisateur));
    }
    public void deletById(Long id) {
        repository.deleteById(id);
    }
    public Utilisateurs save(Utilisateurs utilisateur) {
        return repository.save(utilisateur);
    }
    public Utilisateurs update(Utilisateurs utilisateurs) {
        return repository.save(utilisateurs);
    }
    public void delete(Long id) {
        repository.deleteById(id);
    }
    public boolean updateUtilisateur(Long id, Utilisateurs utilisateursUpdate) {
        Optional<Utilisateurs> optionalUtilisateur = repository.findById(id);

        if (optionalUtilisateur.isPresent()) {
            try {
                Utilisateurs utilisateur = optionalUtilisateur.get();

                utilisateur.setNom(utilisateursUpdate.getNom());
                utilisateur.setPrenom(utilisateursUpdate.getPrenom());
                utilisateur.setEmail(utilisateursUpdate.getEmail());
                utilisateur.setNom_utilisateur(utilisateursUpdate.getNom_utilisateur());
                utilisateur.setMot_de_passe(utilisateursUpdate.getMot_de_passe());
                repository.save(utilisateur);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }

}
