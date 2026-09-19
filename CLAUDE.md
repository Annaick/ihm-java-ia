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
  widget d'agent vocal accessible sur le site. Structure UX inspirée d'un
  portail immobilier classique (hero + recherche superposée, catalogue avec
  sidebar de filtres, fiche bien dynamique `/annonces/{id}`) — design, code,
  textes et images 100% originaux, palette violet + terracotta, icônes
  Bootstrap Icons, typographie Fraunces/système.
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

- **Déployé et en ligne** : https://immobilier.anaick.com (serveur perso,
  `ssh oracle`).
- Convention du serveur (observée sur les autres projets perso avant de
  déployer) : chaque projet vit dans `~/apps/<projet>/`, avec un
  `Dockerfile` à la racine et `deploy/docker-compose.yml` +
  `deploy/Jenkinsfile` + `deploy/README.md`. Caddy (hôte, pas dockerisé)
  route `<projet>.anaick.com` vers `127.0.0.1:<port>` via
  `/etc/caddy/Caddyfile`.
- horizon-immo : conteneur `horizon-immo` sur `127.0.0.1:8081`, volume
  Docker nommé pour persister la base SQLite entre les redéploiements.
- **Jenkins** est dockerisé sur le serveur (`~/apps/jenkins/`,
  `jenkins.anaick.com`) mais **le job "horizon-immo" n'a pas pu être créé
  par Claude** : le mode sandbox bloque la lecture des fichiers de
  credentials/config Jenkins (protection volontaire). À créer manuellement
  par l'utilisateur — voir `PROGRESS.md` § "À faire par l'utilisateur" pour
  la marche à suivre exacte. En attendant, redéploiement manuel :
  `cd ~/apps/horizon-immo && git pull && docker compose -p horizon-immo -f deploy/docker-compose.yml up -d --build`.

## Livrables

1. `UX Research.xlsx` (fait, dans `ihm/`) — pitch produit, 2 personas,
   parcours utilisateur, en français.
2. Maquettes / design — premier jet généré par l'agent IA de Figma (export
   dans `ihm/design export/`), **remplacé depuis par une refonte codée
   directement** (voir `PROGRESS.md` § "refonte du design"). Le fichier
   Figma reste à mettre à jour pour refléter cette nouvelle direction
   (demande explicite de l'utilisateur, pas encore faite).
3. Code Java (fait) — Spring Boot + SQLite, site vitrine + backoffice avec
   authentification (voir Structure du code).
4. Intégration de l'agent vocal x.ai (fait côté code, **pas encore testée
   avec une vraie clé** — voir `PROGRESS.md`).
5. Déploiement (fait) — https://immobilier.anaick.com. Pipeline Jenkins
   encore à créer manuellement (voir ci-dessus).

## Méthode de travail

- Avancer **étape par étape**, en commençant toujours par l'étape la plus
  utile/structurante à ce moment du projet plutôt que d'attaquer plusieurs
  fronts en parallèle.
- Tenir `PROGRESS.md` à jour à chaque étape franchie ou décision prise.

## État d'avancement

Voir `PROGRESS.md`.
