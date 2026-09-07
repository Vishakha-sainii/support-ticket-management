# Architecture Specification

## 1. Architecture Style

The application shall use a modular monolith architecture.

The backend shall use a layered Spring Boot architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

The domain/business rules shall remain independent of HTTP concerns where practical.

The frontend shall communicate with the backend exclusively through REST APIs.

---

# 2. Technology Stack

| Layer           | Technology                          |
| --------------- | ----------------------------------- |
| Language        | Java 21                             |
| Backend         | Spring Boot                         |
| Build           | Gradle                              |
| API             | REST                                |
| Persistence     | Spring Data JPA                     |
| Database        | PostgreSQL                          |
| Test Database   | H2/Testcontainers where appropriate |
| Frontend        | React                               |
| Version Control | Git/GitHub                          |
| IDE/AI          | Cursor                              |

---

# 3. Backend Structure

Recommended structure:

```text
backend/
└── src/
    ├── main/
    │   ├── java/
    │   │   └── .../
    │   │       ├── controller/
    │   │       ├── service/
    │   │       ├── repository/
    │   │       ├── entity/
    │   │       ├── dto/
    │   │       ├── exception/
    │   │       ├── mapper/
    │   │       └── config/
    │   └── resources/
    │       └── application.yml
    │
    └── test/
        └── java/
```

Package names should follow the project's actual base package.

---

# 4. Layer Responsibilities

## Controller

Responsible for:

* HTTP request handling
* request/response mapping
* validation trigger
* HTTP status codes

Controllers must not contain business rules.

## Service

Responsible for:

* business logic
* state transitions
* orchestration
* transaction boundaries

## Repository

Responsible for:

* persistence
* database queries

## Entity

Responsible for persistence representation.

## DTO

Responsible for API contracts.

## Exception

Responsible for domain/application-specific errors.

---

# 5. Error Handling

Use a centralized exception handling mechanism such as `@RestControllerAdvice`.

Errors should return a consistent structure.

Example:

```json
{
  "timestamp": "2026-01-01T10:00:00Z",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Request validation failed",
  "path": "/api/tickets"
}
```

The exact implementation may evolve while preserving the API contract.

---

# 6. Validation

Use Jakarta Bean Validation where appropriate.

Examples:

* `@NotBlank`
* `@Size`
* `@NotNull`

Business validation that cannot be expressed through annotations belongs in the service/domain layer.

---

# 7. Transactions

Operations that modify persistent state shall use appropriate transaction boundaries.

A ticket state transition shall be atomic.

Adding a comment shall persist the comment consistently with the associated ticket.

---

# 8. Database

PostgreSQL is the primary application database.

Database schema evolution should be managed through an appropriate migration mechanism such as Flyway if introduced by the implementation.

Credentials must come from environment/configuration and must never be hardcoded.

---

# 9. Frontend Architecture

Recommended:

```text
frontend/
├── src/
│   ├── components/
│   ├── pages/
│   ├── services/
│   ├── types/
│   ├── hooks/
│   └── utils/
└── ...
```

The frontend shall:

* call backend APIs
* display ticket information
* provide forms
* validate obvious input
* display backend errors
* provide search/filter controls
* provide status transition controls

The frontend must not be responsible for enforcing the authoritative state machine.

---

# 10. Dependency Direction

```text
Controller
   ↓
Service
   ↓
Repository
   ↓
Database
```

Dependencies must not flow backwards.

Controllers must not directly access repositories when business logic is required.

---

# 11. Simplicity Principle

Do not introduce:

* microservices
* Kafka
* Redis
* distributed transactions
* complex event-driven architecture

unless a later requirement explicitly requires them.

