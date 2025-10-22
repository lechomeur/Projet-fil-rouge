-- =============================
-- SUPPRESSION DES TABLES (ordre correct)
-- =============================

DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS utilisateurs;

-- =============================
-- TABLE UTILISATEURS
-- =============================

CREATE TABLE utilisateurs (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              nom VARCHAR(15) NOT NULL,
                              prenom VARCHAR(15) NOT NULL,
                              nom_utilisateur VARCHAR(20) NOT NULL,
                              email VARCHAR(30) NOT NULL,
                              mot_de_passe VARCHAR(255),
                              role VARCHAR(20) DEFAULT 'ADHERENT'
);

INSERT INTO utilisateurs (nom, prenom, nom_utilisateur, email, mot_de_passe, role)
VALUES
    ('Dupont', 'Marie', 'mdupont', 'marie.dupont@email.com', HASH('MD5', 'toto'), 'ADMIN'),
    ('Toto', 'Curie', 'toto', 'curie.toto@email.com','toto', 'ADMIN'),
    ('Bernard', 'Lucas', 'lbernard', 'lucas.bernard@email.com', HASH('MD5', 'toto'), 'TRESORIER'),
    ('Martin', 'Sophie', 'smartin', 'sophie.martin@email.com', HASH('MD5', 'toto'), 'TRESORIER'),
    ('Durand', 'Paul', 'pdurand', 'paul.durand@email.com', HASH('MD5', 'toto'), 'ADHERENT'),
    ('Petit', 'Emma', 'epetit', 'emma.petit@email.com', HASH('MD5', 'toto'), 'ADHERENT'),
    ('Robert', 'Hugo', 'hrobert', 'hugo.robert@email.com', HASH('MD5', 'toto'), 'ADHERENT'),
    ('Richard', 'Lina', 'lrichard', 'lina.richard@email.com', HASH('MD5', 'toto'), 'ADHERENT'),
    ('Moreau', 'Tom', 'tmoreau', 'tom.moreau@email.com', HASH('MD5', 'toto'), 'ADHERENT'),
    ('Laurent', 'Chloé', 'claurent', 'chloe.laurent@email.com', HASH('MD5', 'toto'), 'ADHERENT'),
    ('Simon', 'Nathan', 'nsimon', 'nathan.simon@email.com', HASH('MD5', 'toto'), 'ADHERENT');

-- =============================
-- TABLE TRANSACTIONS
-- =============================

CREATE TABLE transactions (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              type VARCHAR(20) NOT NULL,                -- ENTREE ou SORTIE
                              categorie VARCHAR(30),                    -- COTISATION, DON, ACHAT, AUTRE...
                              montant DECIMAL(10,2) NOT NULL,
                              description VARCHAR(255),
                              date_transaction TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              utilisateur_id INT,                       -- membre concerné
                              valide_par INT,                           -- trésorier ou admin qui valide
                              CONSTRAINT fk_tr_utilisateur
                                  FOREIGN KEY (utilisateur_id)
                                      REFERENCES utilisateurs(id)
                                      ON DELETE CASCADE
                                      ON UPDATE CASCADE,
                              CONSTRAINT fk_tr_tresorier
                                  FOREIGN KEY (valide_par)
                                      REFERENCES utilisateurs(id)
                                      ON DELETE CASCADE
                                      ON UPDATE CASCADE
);

-- =============================
-- INSERTION DE TRANSACTIONS
-- =============================

-- 💰 Entrées : cotisations payées par des membres
INSERT INTO transactions (type, categorie, montant, description, utilisateur_id, valide_par)
VALUES
    ('ENTREE', 'COTISATION', 30.00, 'Cotisation annuelle de Paul', 5, 3),
    ('ENTREE', 'COTISATION', 30.00, 'Cotisation annuelle de Emma', 6, 3),
    ('ENTREE', 'COTISATION', 30.00, 'Cotisation annuelle de Hugo', 7, 4),
    ('ENTREE', 'COTISATION', 30.00, 'Cotisation annuelle de Lina', 8, 4);

-- 🎁 Entrées : dons externes ou subventions
INSERT INTO transactions (type, categorie, montant, description, valide_par)
VALUES
    ('ENTREE', 'DON', 200.00, 'Don du sponsor local', 3),
    ('ENTREE', 'SUBVENTION', 500.00, 'Subvention municipale annuelle', 1);

-- 🛒 Sorties : achats et dépenses
INSERT INTO transactions (type, categorie, montant, description, valide_par)
VALUES
    ('SORTIE', 'ACHAT', 150.00, 'Achat de matériel pour événement sportif', 2),
    ('SORTIE', 'ACHAT', 80.00, 'Achat de fournitures de bureau', 3),
    ('SORTIE', 'EVENEMENT', 250.00, 'Location de salle pour réunion annuelle', 4);

-- 💼 Entrées : activités spéciales
INSERT INTO transactions (type, categorie, montant, description, valide_par)
VALUES
    ('ENTREE', 'VENTE', 100.00, 'Vente de t-shirts de l’association', 2),
    ('ENTREE', 'PARTICIPATION', 50.00, 'Participation aux ateliers du mois', 1);

-- 💵 Sortie
INSERT INTO transactions (type, categorie, montant, description, utilisateur_id, valide_par)
VALUES
    ('SORTIE', 'REMBOURSEMENT', 20.00, 'Remboursement d’avance à Nathan', 11, 3);
