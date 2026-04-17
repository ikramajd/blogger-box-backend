# Blogger Box Backend

Spring Boot REST API for the Blogger Box application. The backend manages blog posts and categories, exposes documented REST endpoints, validates requests, and can run with either PostgreSQL or an in-memory H2 database for local demos.

## Features

- REST API with Spring Boot
- `Post` and `Category` JPA entities
- CRUD operations for posts
- CRUD operations for categories
- Search posts by title or content
- Filter posts by creation date
- List posts by category
- Request validation with Jakarta Validation
- Global JSON error responses
- Seed data with 10 categories and 15 sample posts
- Swagger UI documentation
- CORS configuration for Angular
- PostgreSQL support
- H2 test/demo profile

## Tech Stack

- Java 25
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Hibernate
- PostgreSQL
- H2
- SpringDoc OpenAPI
- Maven

## Run Locally

For the easiest demo mode, run with the H2 test profile:

```bash
cd /Users/ikram/Desktop/blogger-box-backend
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=test
```

The API runs on:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

## PostgreSQL Configuration

The default configuration expects PostgreSQL:

```text
jdbc:postgresql://localhost:5432/blogger_box
postgres / postgres
```

You can override it with environment variables:

```bash
export DB_URL="jdbc:postgresql://HOST:5432/postgres"
export DB_USERNAME="postgres"
export DB_PASSWORD="your-password"
```

The SQL schema is available in:

```text
database/schema.sql
```

## Main Endpoints

### Categories

- `GET /v1/categories`
- `GET /v1/categories?name=tech`
- `GET /v1/categories/{id}`
- `GET /v1/categories/{id}/posts`
- `POST /v1/categories`
- `PUT /v1/categories/{id}`
- `PATCH /v1/categories/{id}`
- `DELETE /v1/categories/{id}`

### Posts

- `GET /v1/posts`
- `GET /v1/posts?value=java`
- `GET /v1/posts?date=2026-04-17`
- `GET /v1/posts/{id}`
- `POST /v1/posts`
- `PUT /v1/posts/{id}`
- `DELETE /v1/posts/{id}`

## Validation Example

Invalid requests return a structured JSON response:

```json
{
  "timestamp": "2026-04-17T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "title: Title is required",
  "path": "/v1/posts"
}
```

## Tests

```bash
mvn test
```

## Screenshots

Add screenshots in the frontend README. Recommended backend screenshot:

- Swagger UI: `docs/screenshots/swagger.png`

## GitHub

Repository:

```text
https://github.com/ikramajd/blogger-box-backend
```
