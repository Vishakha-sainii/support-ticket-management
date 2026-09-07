# Support Ticket Management System

A spec-driven support ticket application with a **Spring Boot** REST API, **PostgreSQL** persistence, and a **React** frontend.

## Features

- Create, list, view, and update support tickets
- Add comments to tickets
- Search tickets by keyword (title/description)
- Filter tickets by status
- Controlled ticket lifecycle (OPEN → IN_PROGRESS → RESOLVED → CLOSED, with CANCELLED paths)
- Backend validation and consistent API error responses
- Frontend error display for API failures

## Tech Stack

| Layer | Technology |
|-------|------------|
| Backend | Java 21, Spring Boot 3.4, Gradle |
| Database | PostgreSQL (Flyway migrations) |
| Frontend | React 19, TypeScript, Vite |
| Tests | JUnit 5, Mockito, Vitest, Testing Library |

## Prerequisites

- Java 21+
- Node.js 18+
- PostgreSQL 14+

## Quick Start

### 1. Database

Create a PostgreSQL database and user:

```sql
CREATE DATABASE support_tickets;
CREATE USER support_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE support_tickets TO support_user;
```

### 2. Backend

```bash
cd backend
cp .env.example .env   # edit with your credentials
export $(grep -v '^#' .env | xargs)   # or set env vars manually

./gradlew bootRun
```

API runs at **http://localhost:8080**

Environment variables:

| Variable | Description | Default |
|----------|-------------|---------|
| `DATABASE_URL` | JDBC URL | `jdbc:postgresql://localhost:5432/support_tickets` |
| `DATABASE_USERNAME` | DB user | `support_user` |
| `DATABASE_PASSWORD` | DB password | *(none — must be set)* |

### 3. Frontend

```bash
cd frontend
npm install
npm run dev
```

UI runs at **http://localhost:5173** (proxies `/api` to the backend).

## Running Tests

```bash
# Backend (57 tests)
cd backend && ./gradlew test

# Frontend (13 tests)
cd frontend && npm test
```

## API Overview

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/tickets` | Create ticket (starts OPEN) |
| GET | `/api/tickets` | List tickets (`?search=`, `?status=`) |
| GET | `/api/tickets/{id}` | Get ticket with comments |
| PUT | `/api/tickets/{id}` | Update title, description, priority, assignee |
| PATCH | `/api/tickets/{id}/status` | State transition |
| POST | `/api/tickets/{id}/comments` | Add comment |

See [spec/api-contract.md](spec/api-contract.md) for full details.

## Project Structure

```
backend/          Spring Boot API
frontend/         React UI
spec/             Requirements, API contract, state machine
docs/             Tasks, traceability, review reports
rules/            Engineering conventions
```

## Documentation

- [Implementation tasks](docs/tasks.md)
- [Traceability matrix](docs/traceability-matrix.md)
- [Spec vs code review](docs/spec-code-review.md)
- [Security review](docs/security-review.md)

## Specification

This project follows spec-driven development. The authoritative specifications live in `spec/`:

- `requirements.md` — functional and non-functional requirements
- `api-contract.md` — REST API
- `state-machine.md` — ticket lifecycle rules
- `data-model.md` — entities and constraints
- `ui-flow.md` — frontend flows
- `acceptance-criteria.md` — Given/When/Then criteria
