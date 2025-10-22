package com.omadi.projetfilrouge_v2.repository;

import com.omadi.projetfilrouge_v2.entities.Utilisateurs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Utilisateurs, Long>, JpaSpecificationExecutor<Utilisateurs> {

   Utilisateurs findByNomUtilisateur(String nom_utilisateur);


}
