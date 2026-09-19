# Suivi du projet — Agence Immobilière

## 2026-09-19

- Thème validé : site vitrine agence immobilière + agent IA vocal 24h/24.
- But de l'agent vocal fixé : qualifier le besoin puis booker un RDV avec un
  agent humain ; le RDV est enregistré dans un backoffice.
- Déploiement prévu : serveur perso via `ssh oracle`, domaine
  immobilier.anaick.com.
- `CLAUDE.md`, `PROGRESS.md` et l'espace mémoire créés.

## Prochaine étape

- Explorer `UX Research.xlsx` (sections Product Pitch / UX Persona / User
  Journey) et commencer à le remplir pour l'agence immobilière — c'est la
  base qui doit guider le design puis le code.

## 2026-09-19 (suite)

- Dossier `ihm/` créé, `UX Research.xlsx` déplacé dedans et rempli :
  - **Product Pitch** : produit nommé "Horizon Immo", segment primaire
    (prospects) + segment secondaire (agents immobiliers).
  - **UX Persona** : deux personas sur deux onglets (`2. Exercise - UX
    Persona` = Camille, la prospecte ; `2b. Exercise - UX Persona` = Marc,
    l'agent immobilier).
  - **User Journey** : parcours complet de Camille, de l'arrivée sur le site
    vitrine un dimanche soir jusqu'à la confirmation du RDV, en passant par
    la qualification vocale et le traitement du lead dans le backoffice par
    Marc.

## Prochaine étape

- Créer le design Figma (site vitrine + widget agent vocal + backoffice) à
  partir de ce parcours utilisateur.

## 2026-09-19 (Figma)

- Fichier Figma créé : "Horizon Immo - Site vitrine"
  (https://www.figma.com/design/JzXKW9mePH4T61WgNQTERz), avec 2 pages :
  "Site vitrine" et "Backoffice".
- Librairie Material 3 Design Kit identifiée comme design system à utiliser
  (déjà ajoutée au fichier). Composants/styles repérés : Button, Text field,
  Horizontal card, App bar, FAB (parfait pour le widget de l'agent vocal),
  Filter chip, Generic avatar, variables de couleur M3 (Primary Container,
  Surface, Outline...), text styles M3 (headline/title/body).
- **Bloqué** : quota Figma MCP du plan Starter épuisé (20 appels/mois pour
  les outils de lecture). Impossible de continuer à construire les écrans
  tant que le quota n'est pas réinitialisé (début de mois suivant) ou que le
  plan Figma n'est pas mis à niveau (Professionnel = 200 appels/jour).

## Prochaine étape

- Reprendre la construction des écrans Figma dès que le quota est
  disponible, en réutilisant les clés de composants/variables déjà
  identifiées ci-dessus (éviter de refaire les recherches `search_design_system`).

## 2026-09-19 (contournement quota)

- Quota Figma MCP épuisé pour le mois : au lieu d'attendre, brief de design
  autonome rédigé dans `ihm/brief-design-figma-ai.md`, à donner à l'agent IA
  de design intégré à Figma (Figma Make / agent design) pour qu'il génère
  les écrans directement dans Figma sans passer par nos outils MCP limités.
- Ce brief contient : pitch produit, personas, parcours utilisateur, direction
  visuelle (Material 3), et le détail écran par écran (Accueil, Annonces,
  widget agent vocal, Backoffice dashboard, Backoffice détail lead).

## Prochaine étape

- Donner `ihm/brief-design-figma-ai.md` à l'agent IA de Figma et vérifier le
  résultat une fois généré.

## 2026-09-19 (Excel — version finale FR)

- `ihm/UX Research.xlsx` nettoyé et entièrement traduit en français :
  - Onglets d'exemple (Airbnb / Manu) supprimés.
  - Onglets restants renommés : "1. Pitch produit", "2. Persona - Camille",
    "2b. Persona - Marc", "3. Parcours utilisateur".
  - Tous les libellés du modèle (segments, infos persona, contexte du
    parcours) traduits en français.
  - Lignes d'étapes inutilisées du parcours (12 à 14) nettoyées.
- Le fichier ne contient plus que notre propre analyse, en français.

## Prochaine étape

- Récupérer le design généré par l'agent IA de Figma et l'examiner.

## 2026-09-19 (Git + décisions techniques)

- **Techno choisie** : Java + SQLite pour le backend, **x.ai Voice Agent**
  pour l'agent IA vocal.
- Design Figma terminé par l'utilisateur (agent IA de Figma), export dans
  `ihm/design export/` (5 écrans : Accueil, Annonces, Agent vocal ouvert,
  Dashboard, Lead détail).
- Dépôt Git corrigé : un `.git` avait été initialisé par erreur dans `ihm/`
  (sans aucun commit, supprimé sans risque) ; dépôt réinitialisé à la racine
  du projet, remote `git@github.com:Annaick/ihm-java-ia.git`, premier commit
  poussé sur `main`.
- Domaine `immobilier.anaick.com` déjà redirigé par l'utilisateur vers le
  serveur perso. Jenkins déjà installé sur ce serveur.

## Prochaine étape

- Démarrer le projet Java (structure du projet, choix du framework web,
  schéma SQLite) à partir des écrans du design et du parcours utilisateur.
- Mettre en place le pipeline Jenkins une fois le projet Java amorcé.

## 2026-09-19 (squelette Spring Boot fonctionnel)

- Choix confirmé : **Spring Boot** (Java 21, Maven), Thymeleaf pour le
  rendu serveur, **SQLite** via `sqlite-jdbc` + `hibernate-community-dialects`.
- Projet scaffoldé et **testé de bout en bout en local** :
  - `Property` (biens) + `Lead` (RDV qualifiés par l'agent vocal), avec
    repositories/services Spring Data JPA.
  - Site vitrine : `/` (accueil, biens à la une), `/annonces` (filtres
    achat/location, zone, type, budget max) — Thymeleaf, widget vocal en
    FAB flottant (pas encore connecté à x.ai, lien statique pour l'instant).
  - Backoffice : `/backoffice` (dashboard des leads), `/backoffice/leads/{id}`
    (détail + confirmation/traitement du statut).
  - **API REST pour l'agent vocal x.ai** : `POST /api/leads` (création d'un
    lead qualifié) et `GET /api/properties` (recherche de biens par
    critères) — c'est le point d'intégration à brancher sur le function
    calling de l'agent vocal.
  - Données de démo (5 biens) via `data.sql`.
- Testé avec `mvn spring-boot:run` : les 3 pages web répondent 200, la
  création d'un lead via `POST /api/leads` fonctionne, le lead apparaît dans
  le dashboard, et la confirmation de statut fonctionne.
- Pas encore fait : authentification du backoffice (TODO noté dans
  `BackofficeController`), style aligné sur le design Figma exporté (CSS
  actuel simple et fonctionnel, pas pixel-perfect), intégration réelle de
  l'agent vocal x.ai (widget FAB actuellement statique).

## Prochaine étape

- Intégrer l'agent vocal x.ai : le connecter au widget FAB côté front, et
  au function calling vers `POST /api/leads` / `GET /api/properties`.
- Ajuster le style des templates pour se rapprocher du design Figma
  (`ihm/design export/`).
- Mettre en place le pipeline Jenkins (build Maven + déploiement sur le
  serveur perso, domaine immobilier.anaick.com).

## 2026-09-19 (auth, style, intégration x.ai, déploiement)

- **Authentification backoffice** : Spring Security, compte agent seedé au
  démarrage (`AdminSeeder`) — `admin@immobilier.anaick.com` / `1234567890`
  (mot de passe volontairement simple pour la démo). Page de login stylée,
  déconnexion, protection CSRF vérifiée (login OK, mauvais mot de passe
  rejeté, logout OK).
- **Style** aligné sur le design Figma exporté : palette violette, hero
  deux colonnes avec badge "actif 24h/24", cartes de réassurance, sidebar
  backoffice, chips de statut.
- **Widget agent vocal x.ai** implémenté (`static/js/voice-agent.js`) selon
  le protocole officiel (vérifié sur docs.x.ai) :
  - Backend `POST /api/xai/session` échange `XAI_API_KEY` contre un jeton
    éphémère (`https://api.x.ai/v1/realtime/client_secrets`), jamais exposé
    au navigateur.
  - Frontend : WebSocket `wss://api.x.ai/v1/realtime`, capture micro
    (resampling 24kHz, PCM16), lecture audio, function calling
    (`rechercher_biens` → `/api/properties`, `creer_rendez_vous` →
    `/api/leads`).
  - **Non vérifiable sans clé API réelle** : les noms exacts des événements
    de transcript texte ne sont pas entièrement documentés publiquement ;
    le code gère les motifs les plus probables mais est à ajuster après un
    premier test réel (onglet Réseau du navigateur) une fois la clé
    branchée.
- **Déploiement** effectué sur le serveur perso (`ssh oracle`), en suivant
  exactement la convention des autres projets (`~/apps/<projet>/`,
  `Dockerfile` + `deploy/docker-compose.yml` + `deploy/Jenkinsfile`,
  Caddy `<projet>.anaick.com → reverse_proxy 127.0.0.1:<port>`) :
  - Repo cloné dans `~/apps/horizon-immo`, image Docker buildée, conteneur
    `horizon-immo` lancé sur `127.0.0.1:8081` (volume nommé pour persister
    la base SQLite entre les redéploiements).
  - Bloc Caddy `immobilier.anaick.com` ajouté et rechargé.
  - **https://immobilier.anaick.com est en ligne** (testé, 200, HTTPS
    provisionné automatiquement).
  - **Job Jenkins non créé** : bloqué par le mode sandbox de Claude Code, qui
    interdit la lecture des fichiers de credentials/config Jenkins
    (`CREDENTIALS.txt`, `casc.yaml`, secrets montés dans le conteneur) —
    protection volontaire, non contournée. Voir section "À faire par
    l'utilisateur" ci-dessous.

## À faire par l'utilisateur

1. **Brancher la clé x.ai** : sur le serveur, créer
   `~/apps/horizon-immo/deploy/horizon-immo.env` avec `XAI_API_KEY=xai-...`,
   puis `docker compose -p horizon-immo -f deploy/docker-compose.yml up -d`
   pour relancer le conteneur avec la clé. Tester ensuite le widget vocal
   dans un vrai navigateur (micro + son) et ajuster si besoin les noms
   d'événements de transcript dans `voice-agent.js` (voir commentaire en
   tête du fichier).
2. **Créer le job Jenkins "horizon-immo"** (via l'UI `https://jenkins.anaick.com`,
   identifiants dans `~/apps/jenkins/CREDENTIALS.txt`) : New Item → Pipeline
   → repo `git@github.com:Annaick/ihm-java-ia.git` (credential `github-ssh`
   déjà existant), branche `main`, "Pipeline script from SCM",
   `deploy/Jenkinsfile`. Copier le token distant depuis
   `~/apps/jenkins/secrets/DEPLOY_TOKEN` dans "Trigger builds remotely" pour
   retrouver l'URL `https://jenkins.anaick.com/buildByToken/build?job=horizon-immo&token=...`
   comme pour les autres projets.
3. Changer le mot de passe admin backoffice avant tout usage réel (fait pour
   la démo, pas pour la prod).

## 2026-09-19 (refonte du design)

- Premier design jugé trop générique/"IA" (violet omniprésent, emojis,
  aucune animation, lien backoffice public). Refonte complète :
  - Palette élargie : violet (marque) + terracotta (accent/CTA) + fond
    crème, au lieu du tout-violet.
  - Typographie : titres en serif (Fraunces via Google Fonts) + corps en
    police système, pour sortir du look "SaaS générique".
  - Emojis remplacés par un pack d'icônes (Bootstrap Icons, via CDN).
  - Animation : pulsation continue sur le bouton de l'agent vocal (FAB)
    pour attirer l'œil, transitions au survol sur boutons/cartes.
  - Lien "Accès Agent Backoffice" retiré de la navigation publique
    (convention façon WordPress : l'agent va directement sur `/backoffice`,
    qui reste protégé par Spring Security).
  - Vraies photos immobilières utilisées (fournies par l'utilisateur dans
    `ihm/images/`) à la place des placeholders picsum.photos.
  - **Architecture repensée en s'inspirant de la structure UX d'un portail
    immobilier de référence** (`vacation-rentals.realhomes.io`, un thème
    WordPress commercial) : **structure et principes repris (hero plein
    cadre + barre de recherche superposée, sidebar de filtres sur le
    catalogue, fiche bien dédiée), mais design, code, textes et images 100%
    originaux** — pas de code/CSS/images/texte copiés du site de référence
    (question de droits d'auteur sur un thème commercial).
  - **Nouvelle page dynamique `/annonces/{id}`** : fiche bien complète
    (photo, prix, caractéristiques, description, biens similaires).
  - Filtres repensés : formulaire de recherche sur l'accueil (transaction,
    quartier, type, budget) + sidebar filtrable sur le catalogue (types de
    biens dynamiques depuis la base, budget, quartier).
- Vérifié visuellement dans un vrai navigateur (Claude in Chrome) : accueil,
  catalogue, fiche bien — tout s'affiche et se filtre correctement.
- Redéployé en production : https://immobilier.anaick.com reflète le nouveau
  design.
- **Rappel** : l'utilisateur veut mettre à jour le design Figma pour qu'il
  corresponde à cette nouvelle direction visuelle, une fois validée.

## Prochaine étape

- Recueillir le retour de l'utilisateur sur cette nouvelle direction visuelle.
- Si validée : mettre à jour le design Figma pour y correspondre (à la
  demande de l'utilisateur).
- Toujours en attente : clé x.ai, job Jenkins (voir "À faire par
  l'utilisateur" ci-dessus).

## 2026-09-19 (palette orange/beige, header flottant)

- Violet retiré partout, remplacé par orange + beige + brun (tokens CSS
  primitifs/semantiques renommés).
- Header devenu flottant (pilule inset en haut de page, `position: fixed`),
  se transforme en barre plein-largeur au scroll via `static/js/site.js`.
  Bouton "Parler à un spécialiste" (icône casque) ajouté dans le header.
- Corrigé le chevauchement barre de recherche / cartes de réassurance.

## 2026-09-19 (agent vocal x.ai — architecture Voice Agent Builder)

- **Changement d'architecture important** : l'utilisateur a montré que
  x.ai propose un vrai console no-code, **Voice Agent Builder**
  (console.x.ai/voice/agents), où l'agent (instructions, voix, outils
  "API Request") se configure dans un tableau de bord, pas dans notre code.
  On est passés de l'ancienne approche (jeton éphémère + tool-calling géré
  par le JS du navigateur) à un **proxy WebSocket côté serveur**
  (`VoiceProxyHandler`) : le navigateur se connecte à `/ws/voice` sur notre
  propre serveur, qui relaie vers `wss://api.x.ai/v1/realtime?agent_id=...`
  avec la clé API en header `Authorization` (jamais exposée au client).
  Les outils de l'agent appellent **directement** nos endpoints publics
  (`/api/properties`, `/api/leads`, `/api/disponibilites`) — plus besoin de
  logique de function-calling côté navigateur.
- `XaiProperties` simplifié : `xai.api-key` (`XAI_API_KEY`) + `xai.agent-id`
  (`XAI_AGENT_ID`). `XaiSessionController` supprimé, remplacé par
  `XaiConfigController` (juste `{configured: bool}`).
- **L'agent lui-même doit être créé par l'utilisateur** dans le dashboard
  x.ai (accès à son compte requis) — instructions et config des outils
  fournies dans le chat, à coller telles quelles. Voir aussi
  `deploy/README.md` pour l'emplacement exact de `XAI_API_KEY` /
  `XAI_AGENT_ID` sur le serveur.
- Non testé en conditions réelles (pas encore de clé/agent_id fournis) —
  les noms exacts de certains événements de transcript restent à valider
  au premier essai (voir commentaire en tête de `voice-agent.js`).

## 2026-09-19 (backoffice : catalogue dynamique + disponibilités)

- **Gestion du catalogue** (`/backoffice/biens`) : CRUD complet (créer,
  modifier, supprimer un bien). Modifier ici met a jour en temps réel le
  site vitrine ET l'API que l'agent vocal interroge.
- **Disponibilités** (`/backoffice/disponibilites`) : 7 jours seedés par
  défaut (lundi-samedi 9h-19h, dimanche fermé), modifiables, exposés via
  `GET /api/disponibilites` pour que l'agent vocal propose des créneaux
  cohérents avec les horaires réels de l'agence.
- Navigation backoffice factorisée dans `backoffice/nav.html`.
- Testé de bout en bout (création/modification/suppression d'un bien,
  vérifié immédiatement sur `/api/properties`).
- Déployé en production.

## Prochaine étape

- L'utilisateur doit créer l'agent dans console.x.ai/voice/agents avec les
  instructions et les 3 outils "API Request" fournis dans le chat, puis
  renseigner `XAI_API_KEY` et `XAI_AGENT_ID` dans
  `deploy/horizon-immo.env` sur le serveur et redéployer.
- Tester l'agent vocal en conditions réelles une fois configuré, ajuster
  `voice-agent.js` si les noms d'événements de transcript diffèrent de ce
  qui est anticipé.
