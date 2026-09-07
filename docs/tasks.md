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

