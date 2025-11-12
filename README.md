# ProjetFilRouge_v2 - API d'Authentification et Gestion d'Utilisateurs

## Description

Cette API est développée avec **Spring Boot** et **H2** (base de données en mémoire).  
Elle permet de :

- Authentifier les utilisateurs (`login`, `register`) via JWT.
- Gérer les tokens d’accès et refresh tokens.
- Ajouter, modifier et supprimer des utilisateurs (opérations réservées aux **admins**).
- Actualiser un token (`refresh`) et se déconnecter (`logout`).

L’API est sécurisée et utilise un système **access token / refresh token** pour protéger les routes sensibles.

---

## Structure des routes

### Authentification

| Méthode | Route           | Description                     |
|---------|----------------|---------------------------------|
| POST    | `/auth/register` | Crée un nouvel utilisateur et renvoie access & refresh tokens |
| POST    | `/auth/login`    | Authentifie un utilisateur et renvoie access & refresh tokens |
| POST    | `/auth/refresh`  | Génère un nouveau access token à partir du refresh token |
| POST    | `/auth/logout`   | Supprime le refresh token (déconnexion) |

### Gestion des utilisateurs (admin seulement)

| Méthode | Route                     | Description                     |
|---------|----------------------------|---------------------------------|
| POST    | `/users`                  | Ajouter un utilisateur          |
| PUT     | `/users/{id}`             | Modifier un utilisateur         |
| DELETE  | `/users/{id}`             | Supprimer un utilisateur        |
| GET     | `/users`                  | Lister tous les utilisateurs    |
| GET     | `/users/{id}`             | Récupérer un utilisateur        |

> ⚠️ Ces routes nécessitent un **access token** d’un utilisateur avec le rôle `ADMIN`.

---

## Lancer l’API

1. Cloner le projet :

```bash
git clone https://github.com/ton-username/ProjetFilRouge_v2.git
cd ProjetFilRouge_v2
