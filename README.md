# Blogger Box Backend

Backend Spring Boot pour la plateforme de blog des sessions 03 et 04.

## Prerequis

- Java 25
- Maven
- PostgreSQL ou Supabase

## Base de donnees

Executer le script SQL dans Supabase ou PostgreSQL :

```bash
database/schema.sql
```

Puis configurer les variables d'environnement :

```bash
export DB_URL="jdbc:postgresql://HOST:5432/postgres"
export DB_USERNAME="postgres"
export DB_PASSWORD="your-password"
```

Sans variable, l'application essaie d'utiliser :

```text
jdbc:postgresql://localhost:5432/blogger_box
postgres / postgres
```

## Lancer

```bash
mvn spring-boot:run
```

Pour lancer sans PostgreSQL, avec une base H2 en memoire :

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=test
```

## URLs utiles

- API : `http://localhost:8080`
- Swagger : `http://localhost:8080/swagger-ui/index.html`

## Endpoints principaux

- `GET /v1/categories`
- `GET /v1/categories?name=Child`
- `GET /v1/categories/{id}`
- `GET /v1/categories/{id}/posts`
- `POST /v1/categories`
- `PUT /v1/categories/{id}`
- `PATCH /v1/categories/{id}`
- `DELETE /v1/categories/{id}`
- `GET /v1/posts`
- `GET /v1/posts?value=java`
- `GET /v1/posts?date=2026-04-10`
- `GET /v1/posts/{id}`
- `POST /v1/posts`
- `PUT /v1/posts/{id}`
- `DELETE /v1/posts/{id}`
