# Deploy — branche `main` → `immobilier.anaick.com`

## Pièces

| Où | Quoi |
|----|------|
| `Dockerfile` (racine du repo) | Build multi-stage Maven, jar Spring Boot, tourne `java -jar app.jar` sur `:8080` |
| `deploy/docker-compose.yml` | Lance l'image comme conteneur `horizon-immo`, publié sur `127.0.0.1:8081`, volume nommé pour persister la base SQLite |
| `deploy/Jenkinsfile` | Pipeline : checkout `main` → build image → `compose up -d` → smoke test |
| Serveur Oracle `~/apps/jenkins/` | Jenkins dockerisé (`jenkins.anaick.com`), CLI docker + compose déjà dispo, socket docker de l'hôte monté |
| Caddy hôte `/etc/caddy/Caddyfile` | TLS + reverse proxy : `immobilier.anaick.com → :8081` |

## Déploiement semi-automatique

Push sur `main`, puis déclenche le build (curl, ou webhook GitHub) :

```
curl -fsSL "https://jenkins.anaick.com/buildByToken/build?job=horizon-immo&token=<DEPLOY_TOKEN>"
```

`<DEPLOY_TOKEN>` est le même que celui utilisé pour les autres projets, stocké sur le serveur à `~/apps/jenkins/secrets/DEPLOY_TOKEN`.

### Webhook GitHub (optionnel, déploiement 100% automatique sur push)

Repo → Settings → Webhooks → Add webhook
- Payload URL : l'URL ci-dessus
- Content type : `application/x-www-form-urlencoded`
- Events : *Just the push event*

## Déploiement manuel depuis le serveur

```bash
cd ~/apps/horizon-immo
git fetch origin && git reset --hard origin/main
docker compose -p horizon-immo -f deploy/docker-compose.yml up -d --build
```

## Agent vocal x.ai

`XAI_API_KEY` est lue au runtime (voir `com.horizonimmo.config.XaiProperties`).
Pour activer l'agent vocal, ajoute-la dans **`deploy/horizon-immo.env`** sur le
serveur (fichier non versionné) :

```
XAI_API_KEY=xai-...
```

Puis redéploie (`docker compose ... up -d` reprend le fichier `env_file`
automatiquement). Sans cette variable, le site fonctionne normalement mais
le widget vocal affiche "agent vocal pas encore configuré".

## Compte agent backoffice

Identifiants créés automatiquement au premier démarrage (voir
`AdminSeeder`) : `admin@immobilier.anaick.com` / `1234567890`. À changer
avant tout usage réel — ce mot de passe est volontairement simple pour la
démo du projet école.
