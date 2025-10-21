package com.omadi.projetfilrouge_v2.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Utilisateurs {

    @Id
    private Long id;
    private String nom;
    @JsonIgnore
    private String mot_de_passe;
    private String prenom;
    @Column(name = "nom_utilisateur") // mapping vers la colonne SQL
    private String nomUtilisateur;    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMot_de_passe() {
        return mot_de_passe;
    }

    public void setMot_de_passe(String mot_de_passe) {
        this.mot_de_passe = mot_de_passe;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom_utilisateur() {
        return nomUtilisateur;
    }

    public void setNom_utilisateur(String nom_utilisateur) {
        this.nomUtilisateur = nom_utilisateur;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
