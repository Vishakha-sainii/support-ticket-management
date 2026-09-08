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
- Authentication with role-based access control (ADMIN / USER)
- Mandatory field validation with visual indicators
- Priority and status badge styling
- Full-screen responsive layout

## Tech Stack

| Layer | Technology |
|-------|------------|
| Backend | Java 21, Spring Boot 3.4, Gradle, Spring Security |
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

## Demo Authentication (Local Development Only)

> **Warning:** The credentials below are **development/demo credentials only**. They are intended for local evaluation of this assignment. **Do not use these credentials in production.** Configure proper authentication for any production deployment.

| Role | Username | Password |
|------|----------|----------|
| ADMIN | `admin` | `admin123` |
| USER | `user` | `user123` |

- **ADMIN** can view, create, and update tickets.
- **USER** can view and update tickets but **cannot create** tickets.

Log in through the UI login screen. The backend enforces authorization — frontend role checks are for usability only.

Demo users are configured in `application.yml` (not embedded in business logic). Passwords may be overridden via environment variables:

| Variable | Description | Default |
|----------|-------------|---------|
| `DEMO_ADMIN_PASSWORD` | ADMIN demo password | `admin123` |
| `DEMO_USER_PASSWORD` | USER demo password | `user123` |

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
| POST | `/api/auth/login` | Authenticate (establish session) |
| GET | `/api/auth/me` | Get current user and role |
| POST | `/api/auth/logout` | End session |
| POST | `/api/tickets` | Create ticket (ADMIN only, starts OPEN) |
| GET | `/api/tickets` | List tickets (`?search=`, `?status=`) |
| GET | `/api/tickets/{id}` | Get ticket with comments |
| PUT | `/api/tickets/{id}` | Update title, description, priority, assignee |
| PATCH | `/api/tickets/{id}/status` | State transition |
| POST | `/api/tickets/{id}/comments` | Add comment |

All ticket endpoints require authentication.

See [spec/api-contract.md](spec/api-contract.md) for full details.

**cURL examples:** [docs/api-curl-examples.md](docs/api-curl-examples.md)

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
- [API cURL examples](docs/api-curl-examples.md)
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
