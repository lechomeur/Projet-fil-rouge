package com.omadi.projetfilrouge.repository;

import com.omadi.projetfilrouge.entities.Utilisateurs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<Utilisateurs, Long>, JpaSpecificationExecutor<Utilisateurs> {

   // Utilisateurs findByNomUtilisateur(String nom_utilisateur);
}
