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
- **Backend Java web client-serveur** : sert le vitrine + le backoffice,
  expose une API pour que l'agent vocal puisse créer un RDV. Techno précise à
  valider selon les consignes de l'école (Spring Boot ? Servlets/JSP ?).
- **Agent IA vocal** : piste explorée = API voice de x.ai (Grok), à tester via
  l'essai gratuit avant d'engager des frais. Alternatives à garder en tête si
  ça ne convient pas.

## Déploiement

- Serveur personnel de l'utilisateur, accessible via `ssh oracle`.
- Domaine prévu : **immobilier.anaick.com**, sur le même modèle que les autres
  projets persos déjà déployés sur ce serveur.
- Avant de configurer quoi que ce soit sur le serveur : observer comment les
  autres projets persos y sont déployés (reverse proxy, process manager,
  certificats) pour rester cohérent, plutôt que de réinventer une convention.

## Livrables

1. `UX Research.xlsx` (déjà présent) — modèle imposé, 3 sections : Product
   Pitch, UX Persona, User Journey (chacune avec un exemple et un exercice à
   remplir).
2. Maquettes / design (Figma).
3. Code Java du site vitrine + backoffice.
4. Intégration de l'agent vocal + connexion à la prise de RDV.

## Méthode de travail

- Avancer **étape par étape**, en commençant toujours par l'étape la plus
  utile/structurante à ce moment du projet plutôt que d'attaquer plusieurs
  fronts en parallèle.
- Tenir `PROGRESS.md` à jour à chaque étape franchie ou décision prise.

## État d'avancement

Voir `PROGRESS.md`.
