# Notes Figma — Horizon Immo

Fichier : https://www.figma.com/design/JzXKW9mePH4T61WgNQTERz
Pages : "Site vitrine" (id 0:1), "Backoffice" (id 1:2)

Librairie utilisée : **Material 3 Design Kit**
`libraryKey = lk-5a31d104cabc6a74d4edf6425e7bc6575e9c0f18cda7efb746193aef4d915b077d115c985e6cf49d36d97d455a17d5127a2cbbfbc618b8a70a38669dccb61462`

Ces clés ont été trouvées via `search_design_system` — à réutiliser directement
avec `importComponentSetByKeyAsync` / `importVariableByKeyAsync` /
`importStyleByKeyAsync` pour ne pas re-consommer le quota Figma MCP (Starter =
20 appels de lecture/mois, épuisé le 2026-09-19).

## Composants (component_set keys)

- Button : `ab924dce8b851fd820bd0d56da24a9c489311ae6`
- Text field : `a54980c1c99c62912ab39e9e30fe5c267c98edb4`
- Horizontal card : `78d3f1e228edbedd6296fda77df704a36224f12b`
- App bar : `23a20484e255cb14cf467ccc984ac918e457cefb`
- FAB (widget agent vocal) : `d50a484bd3e7c607851a8f29a8384c941ab51ac1`
- Extended FAB : `ccefc26b7e7a59ee386fadd4e3b01a58a357677c`
- Filter chip : `a3c83c534d78babd0613f4f14c72ac25ebae90cf`
- Generic avatar : `3c69ab04554ae4a83ea3553aedbd6b1b72b2227d`
- Navigation Drawer : `2e423d599a52f1d1c61335f521227c0db6f184af`

## Variables couleur (M3 collection)

- Schemes/Primary Container : `a103186b128f8b374dd67aca273d14b16f80c015`
- Schemes/On Primary Container : `3955ea91dec6375cafc103197d010e8f972b48ea`
- Schemes/On Surface : `d708be20513001c4083ed24c1e58c2fdaca456b3`
- Schemes/On Surface Variant : `2669a52b8e7217f9819767cb22af92e9332d8148`
- Schemes/Surface Container : `39429ef475465b43ef4cb71db29f18b87b4abf4a`
- Schemes/Surface Container Lowest : `084fd9eef53493f628f937becb5914405009aa93`
- Schemes/Outline : `fe6df42091f941865aeced3d5f5fd1477728bbcc`
- Schemes/Outline Variant : `62f3600b56f0544c7c806efc996e3a710e7c4314`

## Text styles (M3)

- headline/large : `6bbad1b27fe05ecdb1858533b6a345777d20d506`
- headline/medium : `2b0fde8456dd95575d375d05490a1183fbfbcdb6`
- title/large : `6d152883d5f7e827add2e91513fd9a90bf762baa`
- title/medium : `86640243a9bbd68ce43575cceb59e4256dfec4f3`
- body/large : `4aeaa89a2f980c69d11b84a0e2a4f1268119c277`
- body/medium : `1e92164361231ef1ed79f8fd256e78e0f6fbf250`

## Plan d'écrans (pas encore construits)

1. **Site vitrine — Accueil** : App bar (logo + nav), Hero (headline + sous-titre
   + Button), grille de 3 Horizontal card (biens à la une), FAB en bas à droite
   ("Parler à notre conseiller" — agent vocal), footer simple.
2. **Site vitrine — Annonces** : App bar, ligne de Filter chip (achat/location,
   budget, zone, type), liste de Horizontal card.
3. **Widget agent vocal (état conversation)** : petite fenêtre/modal ancrée
   en bas à droite, avatar + bulles de dialogue, Text field ou indicateur
   micro, Button "Confirmer le rendez-vous".
4. **Backoffice — Dashboard** : Navigation Drawer (Leads, Biens, Paramètres),
   App bar, liste de leads (Generic avatar + nom + créneau + statut via
   Filter chip), état "nouveau/confirmé/traité".
5. **Backoffice — Détail d'un lead** : infos qualifiées, résumé de la
   conversation, Button "Confirmer" / "Reproposer un créneau".
