DROP TABLE IF EXISTS utilisateurs;

CREATE TABLE utilisateurs (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              nom VARCHAR(15) NOT NULL,
                              prenom VARCHAR(15) NOT NULL,
                              nom_utilisateur VARCHAR(20) NOT NULL,
                              email VARCHAR(30) NOT NULL,
                              mot_de_passe VARCHAR(255)
);

INSERT INTO utilisateurs (nom, prenom, nom_utilisateur, email, mot_de_passe)
VALUES
    ('Dupont', 'Marie', 'mdupont', 'marie.dupont@email.com', HASH('MD5', 'toto')),
    ('Bernard', 'Lucas', 'lbernard', 'lucas.bernard@email.com', HASH('MD5', 'toto')),
    ('Martin', 'Sophie', 'smartin', 'sophie.martin@email.com', HASH('MD5', 'toto')),
    ('Durand', 'Paul', 'pdurand', 'paul.durand@email.com', HASH('MD5', 'toto')),
    ('Petit', 'Emma', 'epetit', 'emma.petit@email.com', HASH('MD5', 'toto')),
    ('Robert', 'Hugo', 'hrobert', 'hugo.robert@email.com', HASH('MD5', 'toto')),
    ('Richard', 'Lina', 'lrichard', 'lina.richard@email.com', HASH('MD5', 'toto')),
    ('Moreau', 'Tom', 'tmoreau', 'tom.moreau@email.com', HASH('MD5', 'toto')),
    ('Laurent', 'Chloé', 'claurent', 'chloe.laurent@email.com', HASH('MD5', 'toto')),
    ('Simon', 'Nathan', 'nsimon', 'nathan.simon@email.com', HASH('MD5', 'toto'));
