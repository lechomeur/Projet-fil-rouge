package com.omadi.projetfilrouge_v2.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
/**
 * Classe de base abstraite pour toutes les entités JPA du projet.
 *
 * ✅ Objectif :
 *  - Fournir un champ "id" unique et généré automatiquement pour chaque entité.
 *  - Définir des méthodes equals() et hashCode() cohérentes basées sur cet ID.
 *
 * 💡 Cette classe est conçue pour être héritée, pas instanciée directement.
 */
public class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // The initial value is to account for data.sql demo data ids
    @SequenceGenerator(name = "idgenerator", initialValue = 1000)
    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Override
    public int hashCode() {
        if (getId() != null) {
            return getId().hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractEntity that)) {
            return false; // null or not an AbstractEntity class
        }
        if (getId() != null) {
            return getId().equals(that.getId());
        }
        return super.equals(that);
    }
}
