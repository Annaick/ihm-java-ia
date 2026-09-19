# Brief design — Horizon Immo

Site vitrine d'agence immobilière avec agent IA vocal 24h/24. Ce document est
autonome : il contient tout le contexte nécessaire pour générer le design
sans échange préalable.

## 1. Pitch produit

**Horizon Immo** est un site vitrine d'agence immobilière avec un conseiller
virtuel vocal disponible 24h/24. Son rôle n'est **pas** de répondre à des FAQ,
mais de **qualifier le besoin d'un prospect puis lui obtenir un rendez-vous
avec un agent humain** — même en dehors des horaires d'ouverture de l'agence
(soir, week-end). Chaque rendez-vous pris est enregistré dans un **backoffice**
consultable par les agents de l'agence.

Problème résolu : les prospects visitent le site en dehors des horaires
d'agence et repartent sans contact ; l'agence perd des leads faute de
disponibilité humaine 24h/24.

Différenciant : disponibilité permanente sans effectif supplémentaire,
qualification automatique du besoin avant même le premier contact humain.

## 2. Personas

**Camille, 32 ans, cadre, prospecte** — cherche un premier achat immobilier.
Travaille en journée, ne peut se renseigner que le soir ou le week-end.
Frustrée par les sites qui n'offrent qu'un formulaire de contact sans réponse
immédiate. Veut une réponse rapide et un rendez-vous fixé sans attendre.

**Marc, 45 ans, agent immobilier senior** — traite les demandes de rendez-vous
côté agence, dans le backoffice. Reçoit aujourd'hui trop de leads non
qualifiés via un formulaire classique et perd du temps à rappeler des
prospects mal renseignés ou injoignables. Veut une vue centralisée des
demandes, déjà qualifiées, qu'il peut confirmer en quelques clics.

## 3. Parcours utilisateur clé (à représenter dans le design)

1. Camille arrive sur le site vitrine un dimanche soir et consulte des annonces.
2. Elle remarque un bouton "Parler à notre conseiller" toujours visible.
3. Elle clique : l'agent vocal se présente et pose des questions de
   qualification (achat/location, budget, zone, type de bien, nb de pièces,
   délai).
4. L'agent propose un rendez-vous avec un agent humain, demande un créneau
   souhaité et ses coordonnées.
5. L'agent récapitule oralement et confirme le rendez-vous.
6. Le lead qualifié (résumé, coordonnées, créneau) apparaît dans le
   backoffice.
7. Le lundi matin, Marc consulte le nouveau lead dans le backoffice et
   confirme le créneau.

## 4. Direction visuelle

- **Design system** : Material 3 (déjà ajouté au fichier Figma
  "Horizon Immo - Site vitrine" — réutiliser ses composants : Button, Text
  field, Horizontal card, App bar, FAB, Filter chip, Generic avatar, et les
  tokens de couleur/texte M3).
- **Ton** : professionnel et rassurant (on parle d'un achat important), mais
  chaleureux — l'agent vocal doit donner une impression humaine, pas
  robotique.
- **Palette** : dérivée du thème Material 3 par défaut, avec une couleur
  primaire évoquant la confiance/l'immobilier (bleu profond ou vert
  émeraude) plutôt qu'une couleur criarde.
- **Format** : web desktop, largeur de référence 1440px, en gardant les
  écrans utilisables en mobile (le point de contact "widget vocal" doit
  rester accessible en bas d'écran sur mobile aussi).

## 5. Écrans à concevoir

### 5.1 Site vitrine — Accueil
- **App bar** : logo "Horizon Immo", navigation (Accueil, Annonces, Agence),
  bouton de connexion agent (petit lien discret vers le backoffice).
- **Hero** : titre accrocheur, ex. *"Un conseiller à votre écoute, même à
  23h un dimanche."*, sous-titre expliquant le service, bouton principal
  "Parler à notre conseiller".
- **Section biens à la une** : grille de 3 Horizontal card (photo, titre,
  prix, ville, nb pièces).
- **Section réassurance** : 3 points courts (disponible 24h/24, réponse
  immédiate, rendez-vous garanti).
- **FAB** en bas à droite, persistant sur toute la page : icône micro,
  ouvre le widget agent vocal.
- **Footer** : coordonnées agence, horaires d'ouverture (contraste volontaire
  avec le "24h/24" du conseiller vocal).

### 5.2 Site vitrine — Annonces
- App bar identique.
- Ligne de **Filter chip** : type de transaction (achat/location), budget,
  zone, type de bien, nb de pièces.
- Liste/grille de **Horizontal card** (photo, titre, prix, surface, ville).
- FAB agent vocal toujours visible.

### 5.3 Widget agent vocal (état "conversation")
- Petite fenêtre ancrée en bas à droite (comme un chat), ouverte depuis le
  FAB.
- En-tête : avatar/icône du conseiller + statut "En ligne · 24h/24".
- Corps : bulles de dialogue montrant la qualification en cours (ex. l'agent
  demande budget/zone/type de bien), avec indicateur visuel "à l'écoute"
  (onde sonore ou micro actif) plutôt qu'un simple champ de texte, puisque
  l'interaction est vocale.
- Étape finale visible : récapitulatif du rendez-vous proposé (date, heure,
  nom de l'agent humain) + bouton "Confirmer le rendez-vous".

### 5.4 Backoffice — Dashboard (vue Marc)
- **Navigation Drawer** à gauche : Leads / Biens / Paramètres.
- App bar : nom de l'agence, avatar de l'agent connecté.
- Liste des leads sous forme de lignes/cartes : avatar générique, nom du
  prospect, besoin résumé en une ligne, créneau proposé, statut affiché en
  **Filter chip** (Nouveau / Confirmé / Traité).
- Tri par défaut : créneau le plus proche en premier.

### 5.5 Backoffice — Détail d'un lead
- Résumé qualifié par l'agent vocal (achat/location, budget, zone, type de
  bien, délai).
- Résumé texte de la conversation vocale.
- Coordonnées du prospect.
- Actions : bouton "Confirmer le rendez-vous", bouton secondaire "Reproposer
  un créneau".

## 6. Ce qui n'est volontairement pas demandé

- Pas de paiement en ligne, pas de simulation de prêt.
- Pas de FAQ écrite classique — l'agent vocal remplace ce besoin.
- Pas de compte client prospect (Camille n'a pas besoin de se créer un
  compte) ; seul le côté agence (Marc) a une connexion au backoffice.
