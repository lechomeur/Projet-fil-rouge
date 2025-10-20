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

    public Utilisateurs update(Utilisateurs entity) {
        return repository.save(entity);
    }
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
