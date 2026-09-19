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
