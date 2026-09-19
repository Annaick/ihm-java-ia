# Projet École — Agence Immobilière (IHM + Java + IA)

## Contexte

Trois devoirs d'école distincts, unifiés sur un même thème dans ce dossier :

1. **IHM** — document d'analyse UX (Excel, modèle imposé) + design de l'app.
2. **Java** — construire en web client-serveur ce qui a été conçu côté IHM.
3. **IA** — projet indépendant basé sur l'IA.

## Thème retenu

Site vitrine d'une **agence immobilière**, avec un **agent IA vocal disponible
24h/24** qui joue le rôle de conseiller immobilier virtuel.

**Le but de l'agent vocal n'est pas juste de répondre aux questions.** Son
objectif principal est de qualifier le besoin du visiteur (budget, zone, type
de bien) puis de **booker un rendez-vous avec un agent humain de l'agence**.
Chaque rendez-vous pris atterrit dans un **backoffice** consultable par
l'agence.

## Architecture cible (à affiner au fil du projet)

- **Frontend vitrine** : présentation de l'agence, biens en vente/location,
  widget d'agent vocal accessible sur le site.
- **Backoffice** : interface pour les agents humains — liste des leads/RDV
  générés par l'agent vocal, gestion des biens.
- **Backend Java web client-serveur** : **Spring Boot** (Java 21, Maven,
  Thymeleaf pour le rendu serveur), persistance **SQLite** (`sqlite-jdbc` +
  `hibernate-community-dialects`). Sert le vitrine + le backoffice, expose
  une API REST (`/api/leads`, `/api/properties`) pour l'agent vocal.
- **Agent IA vocal** : **x.ai (Grok) Voice Agent**, testé via l'essai gratuit
  avant d'engager des frais. S'intègre via l'API REST ci-dessus (function
  calling).

## Structure du code

- `src/main/java/com/horizonimmo/model` — entités JPA (`Property`, `Lead`).
- `src/main/java/com/horizonimmo/repository` — Spring Data JPA.
- `src/main/java/com/horizonimmo/service` — logique métier.
- `src/main/java/com/horizonimmo/web` — contrôleurs Thymeleaf (site vitrine +
  backoffice).
- `src/main/java/com/horizonimmo/api` — API REST pour l'agent vocal x.ai.
- `src/main/resources/templates` — vues Thymeleaf (fragments partagés dans
  `fragments/layout.html`).
- Lancer en local : `mvn spring-boot:run` (port 8080, base SQLite créée dans
  `data/horizon-immo.db`, données de démo via `data.sql`).

## Dépôt Git

- `git@github.com:Annaick/ihm-java-ia.git`, branche `main`.
- Dépôt initialisé à la racine de ce dossier (pas dans `ihm/`).

## Déploiement

- Serveur personnel de l'utilisateur, accessible via `ssh oracle`.
- Domaine **immobilier.anaick.com** déjà redirigé vers ce serveur par
  l'utilisateur.
- **Jenkins déjà installé sur le serveur** — à utiliser pour le pipeline
  CI/CD une fois le projet Java amorcé (build + déploiement automatique sur
  push).
- Avant de configurer quoi que ce soit d'autre sur le serveur : observer
  comment les autres projets persos y sont déployés (reverse proxy, process
  manager, certificats) pour rester cohérent, plutôt que de réinventer une
  convention.

## Livrables

1. `UX Research.xlsx` (fait, dans `ihm/`) — pitch produit, 2 personas,
   parcours utilisateur, en français.
2. Maquettes / design (fait, généré par l'agent IA de Figma à partir de
   `ihm/brief-design-figma-ai.md`, export dans `ihm/design export/`).
3. Code Java (Java + SQLite) du site vitrine + backoffice.
4. Intégration de l'agent vocal x.ai + connexion à la prise de RDV.
5. Pipeline Jenkins pour le déploiement sur le serveur perso.

## Méthode de travail

- Avancer **étape par étape**, en commençant toujours par l'étape la plus
  utile/structurante à ce moment du projet plutôt que d'attaquer plusieurs
  fronts en parallèle.
- Tenir `PROGRESS.md` à jour à chaque étape franchie ou décision prise.

## État d'avancement

Voir `PROGRESS.md`.
