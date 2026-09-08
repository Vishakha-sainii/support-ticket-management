# Implementation Tasks

## Phase 1 — Project Foundation

### TASK-001 — Backend Project Setup

**Requirements:** NFR-001, NFR-002

**Status:** Complete

Create the Spring Boot Java 21 Gradle backend.

Acceptance:

* [x] project builds
* [x] application starts
* [x] package structure follows architecture

---

### TASK-002 — Database Configuration

**Requirements:** REQ-008

**Status:** Complete

Configure PostgreSQL persistence.

Acceptance:

* [x] application connects successfully
* [x] configuration does not contain hardcoded secrets

---

## Phase 2 — Domain

### TASK-003 — Ticket Domain Model

**Requirements:** REQ-001, REQ-002, REQ-003, REQ-004, REQ-007

**Status:** Complete

Implement Ticket entity/model and enums.

---

### TASK-004 — Comment Domain Model

**Requirements:** REQ-005

**Status:** Complete

Implement Comment entity/model and relationship.

---

## Phase 3 — Persistence

### TASK-005 — Ticket Repository

**Requirements:** REQ-002, REQ-006, REQ-007

**Status:** Complete

Implement ticket persistence and required queries.

---

### TASK-006 — Comment Repository

**Requirements:** REQ-005

**Status:** Complete

Implement comment persistence.

---

## Phase 4 — Ticket APIs

### TASK-007 — Create Ticket

**Requirements:** REQ-001, REQ-009

**Status:** Complete

Implement create-ticket API.

---

### TASK-008 — List and View Tickets

**Requirements:** REQ-002, REQ-003, BR-004

**Acceptance Criteria:** AC-003, AC-004, AC-005

**Status:** Complete

Implement list and details APIs.

---

### TASK-009 — Update Ticket

**Requirements:** REQ-004, REQ-009, BR-004, BR-006

**Acceptance Criteria:** AC-006, AC-021, AC-023

**Status:** Complete

Implement editable ticket fields. Reject requests that include `status` in the PUT body.

---

### TASK-010 — Comments

**Requirements:** REQ-005, REQ-009, BR-004

**Acceptance Criteria:** AC-007, AC-022

**Status:** Complete

Implement comment API.

---

### TASK-011 — Search and Filtering

**Requirements:** REQ-006, REQ-007

**Status:** Complete

Implement keyword search and status filtering.

---

## Phase 5 — State Machine

### TASK-012 — Ticket State Machine

**Requirements:** REQ-009, BR-001, BR-002, BR-003

**Status:** Complete

Implement backend state-transition enforcement.

---

### TASK-013 — State Transition API

**Requirements:** REQ-009

**Status:** Complete

Implement status transition endpoint.

---

## Phase 6 — Error Handling

### TASK-014 — Validation and Exception Handling

**Requirements:** REQ-009, REQ-010

**Status:** Complete

Implement consistent validation and error responses.

---

## Phase 7 — Frontend

### TASK-015 — Frontend Foundation

**Requirements:** REQ-010

**Status:** Complete

Create React frontend.

---

### TASK-016 — Ticket List and Search UI

**Requirements:** REQ-002, REQ-006, REQ-007

**Status:** Complete

Implement ticket listing, search and filtering.

---

### TASK-017 — Ticket Creation UI

**Requirements:** REQ-001, REQ-010

**Status:** Complete

Implement create ticket form.

---

### TASK-018 — Ticket Details and Edit UI

**Requirements:** REQ-003, REQ-004, REQ-010

**Status:** Complete

Implement details, edit and assignee functionality.

---

### TASK-019 — Comments and Status UI

**Requirements:** REQ-005, REQ-009, REQ-010

**Status:** Complete

Implement comments and status transition controls.

---

## Phase 8 — Testing

### TASK-020 — Backend Unit Tests

**Requirements:** All applicable backend requirements

**Status:** Complete

Implement service and domain tests.

---

### TASK-021 — API Integration Tests

**Requirements:** REQ-001 through REQ-009

**Status:** Complete

Implement API/integration tests.

---

### TASK-022 — State Machine Tests

**Requirements:** REQ-009

**Status:** Complete

Implement valid and invalid transition tests.

---

### TASK-023 — Frontend Tests

**Requirements:** REQ-010

**Status:** Complete

Implement important frontend behavior/error tests.

---

## Phase 9 — Verification

### TASK-024 — Specification vs Code Review

**Status:** Complete

Compare implementation against every requirement.

See [docs/spec-code-review.md](spec-code-review.md).

---

### TASK-025 — Code Quality Review

**Status:** Complete

Review architecture, Java, Spring Boot, API and database implementation.

See [docs/code-quality-review.md](code-quality-review.md).

---

### TASK-026 — Security Review

**Status:** Complete

Verify repository and implementation security.

See [docs/security-review.md](security-review.md).

---

### TASK-027 — Traceability

**Status:** Complete

Update requirement → acceptance criteria → task → implementation → test mapping in `docs/traceability-matrix.md`.

---

### TASK-028 — Final Documentation

**Status:** Complete

Complete README and project documentation.

See [README.md](../README.md).

---

## Phase 10 — Authentication, Authorization & UI Enhancements

### TASK-029 — Authentication Foundation

**Requirements:** REQ-011, NFR-005, NFR-006

**Status:** Pending

Add Spring Security, demo-user configuration in `application.yml`, and `UserDetailsService` loading users from config (not business logic).

Acceptance:

* [ ] Spring Security dependency and `SecurityFilterChain` configured
* [ ] Demo users loaded from application configuration
* [ ] Passwords encoded with `PasswordEncoder`
* [ ] No demo credentials embedded in ticket business logic

---

### TASK-030 — Auth API Endpoints

**Requirements:** REQ-011

**Status:** Pending

Implement authentication REST endpoints.

Acceptance:

* [ ] `POST /api/auth/login` authenticates and establishes session
* [ ] `GET /api/auth/me` returns current user and role
* [ ] `POST /api/auth/logout` invalidates session
* [ ] Invalid credentials return meaningful `401` error

---

### TASK-031 — Backend Authorization

**Requirements:** REQ-012

**Status:** Pending

Enforce role-based access on ticket endpoints.

Acceptance:

* [ ] `POST /api/tickets` requires `ADMIN` role
* [ ] Other ticket endpoints require authentication
* [ ] `USER` attempting `POST /api/tickets` receives `403 Forbidden`
* [ ] `AccessDeniedException` handled with consistent `ErrorResponse`

---

### TASK-032 — Frontend Login & Auth Context

**Requirements:** REQ-011

**Status:** Pending

Implement login page, auth context, protected routes, and session-aware API calls.

Acceptance:

* [ ] Login screen with username/password
* [ ] Successful login navigates to ticket list
* [ ] Invalid credentials show meaningful error
* [ ] Unauthenticated users redirected to login
* [ ] API calls use `credentials: include`

---

### TASK-033 — Frontend Role-Aware UI

**Requirements:** REQ-012

**Status:** Pending

Implement role-based UI visibility.

Acceptance:

* [ ] ADMIN sees Create Ticket action
* [ ] USER does not see Create Ticket action
* [ ] USER navigating to create route is redirected with access-denied message
* [ ] Header shows logged-in user and logout

---

### TASK-034 — Frontend Mandatory Field Validation UX

**Requirements:** REQ-013

**Status:** Pending

Enhance forms with required-field indicators and client-side validation.

Acceptance:

* [ ] Asterisk (`*`) on all mandatory field labels
* [ ] Blank mandatory fields prevent submission
* [ ] Whitespace-only values rejected with field-level messages
* [ ] Invalid fields visually indicated
* [ ] Consistent validation across create, update, and comment forms

---

### TASK-035 — Priority Badge Styling

**Requirements:** REQ-014

**Status:** Pending

Implement priority badge/chip component and styling.

Acceptance:

* [ ] Distinct styling for LOW, MEDIUM, HIGH, CRITICAL
* [ ] Used consistently in list and detail views
* [ ] Accessible (readable text, not color-only)

---

### TASK-036 — Status Badge Styling

**Requirements:** REQ-015

**Status:** Pending

Implement status badge/chip component and styling.

Acceptance:

* [ ] Distinct styling for OPEN, IN_PROGRESS, RESOLVED, CLOSED, CANCELLED
* [ ] Used consistently in list and detail views
* [ ] Accessible (readable text, not color-only)

---

### TASK-037 — Full-Screen Responsive Layout

**Requirements:** REQ-016

**Status:** Pending

Update layout to use full viewport width with responsive breakpoints.

Acceptance:

* [ ] Full-width application layout (no narrow centered constraint)
* [ ] Ticket table uses available horizontal space
* [ ] Responsive desktop, tablet, and mobile layouts
* [ ] Forms usable on smaller screens
* [ ] Existing functionality preserved

---

### TASK-038 — Authentication & Authorization Tests

**Requirements:** REQ-011, REQ-012

**Status:** Pending

Add backend tests for authentication and authorization.

Acceptance:

* [ ] Valid ADMIN and USER login tests
* [ ] Invalid credentials test
* [ ] Unauthenticated access returns `401`
* [ ] USER `POST /api/tickets` returns `403`
* [ ] ADMIN allowed operations verified

---

### TASK-039 — Validation Tests

**Requirements:** REQ-013

**Status:** Pending

Add/update backend and frontend validation tests.

Acceptance:

* [ ] Backend tests for missing and whitespace-only required fields
* [ ] Frontend tests for asterisks, blank prevention, whitespace rejection
* [ ] Existing state-machine tests remain intact

---

### TASK-040 — UI Enhancement Tests

**Requirements:** REQ-014, REQ-015, REQ-016

**Status:** Pending

Add frontend tests for badges, role visibility, and layout.

Acceptance:

* [ ] Priority badge rendering tests
* [ ] Status badge rendering tests
* [ ] Role-based Create Ticket visibility tests
* [ ] Layout/responsive smoke tests where practical

---

### TASK-041 — Documentation Updates

**Requirements:** REQ-011, NFR-005

**Status:** Pending

Update README, traceability matrix, and verify spec alignment.

Acceptance:

* [ ] README documents demo credentials with dev-only warning
* [ ] Traceability matrix updated for REQ-011–REQ-016
* [ ] API cURL examples updated for authentication (if applicable)

---

## Task Execution Rule

Tasks shall be implemented incrementally.

Do not implement the entire application in one operation.

Each task should:

1. Read relevant specifications.
2. Implement only the task.
3. Add/update tests.
4. Run relevant tests.
5. Review the result.
6. Mark the task complete.

