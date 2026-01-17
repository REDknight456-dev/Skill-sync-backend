# Skill-Sync API

Spring Boot 3.4 starter implementing JWT auth, RBAC, course catalog, and email notifications.

## Endpoints
- POST /api/auth/register — create user and return JWT
- POST /api/auth/login — authenticate and return JWT
- GET /api/courses — list courses (USER/ADMIN)
- GET /api/courses/{id} — get course details (USER/ADMIN)
- POST /api/courses — create course (ADMIN)
- DELETE /api/courses/{id} — remove course (ADMIN)
- GET /api/users — list users (ADMIN)

## Running locally
1. Set environment values or edit `src/main/resources/application.properties` for mail and JWT secret.
2. Build and run:
   ```bash
   ./mvnw spring-boot:run
   ```

## Notes
- JWT uses HS256 with 24h expiration and embeds `sub` (email) and `role` claim.
- GlobalExceptionHandler returns JSON for access denied, validation, and bad requests.
- Email templates: welcome email and admin notification on course creation.
