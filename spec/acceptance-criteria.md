# Acceptance Criteria

## AC-001 — Create Ticket

**Requirement:** REQ-001

**Given** the user provides valid ticket information
**When** the user submits the create-ticket request
**Then** the system shall create and persist the ticket
**And** the initial status shall be `OPEN`.

---

## AC-002 — Create Ticket Validation

**Requirement:** REQ-001, REQ-009

**Given** required ticket information is missing or invalid
**When** the user submits the request
**Then** the backend shall reject the request
**And** return a meaningful validation error.

---

## AC-003 — List Tickets

**Requirement:** REQ-002

**Given** tickets exist
**When** the user requests the ticket list
**Then** the system shall return the tickets successfully.

---

## AC-004 — View Ticket

**Requirement:** REQ-003

**Given** a ticket exists
**When** the user requests the ticket by ID
**Then** the system shall return its details and comments.

---

## AC-005 — Ticket Not Found

**Requirement:** REQ-003

**Given** a ticket ID does not exist
**When** the user requests that ticket
**Then** the backend shall return a not-found response with a meaningful error.

---

## AC-006 — Update Ticket

**Requirement:** REQ-004

**Given** an existing ticket
**When** valid title, description, priority, or assignee changes are submitted
**Then** the changes shall be persisted.

---

## AC-007 — Add Comment

**Requirement:** REQ-005

**Given** an existing ticket
**When** a valid comment is submitted
**Then** the comment shall be persisted against the ticket.

---

## AC-008 — Search

**Requirement:** REQ-006

**Given** tickets exist
**When** a keyword is supplied
**Then** matching tickets shall be returned.

---

## AC-009 — Status Filter

**Requirement:** REQ-007

**Given** tickets with different statuses exist
**When** a status filter is supplied
**Then** only tickets matching that status shall be returned.

---

## AC-010 — Persistence

**Requirement:** REQ-008

**Given** ticket data has been persisted
**When** the application is restarted
**Then** the data shall remain available.

---

## AC-011 — Valid OPEN Transition

**Requirement:** REQ-001, REQ-009

**Given** a ticket is `OPEN`
**When** it is transitioned to `IN_PROGRESS`
**Then** the transition shall succeed.

---

## AC-012 — Valid IN_PROGRESS Transition

**Requirement:** REQ-009

**Given** a ticket is `IN_PROGRESS`
**When** it is transitioned to `RESOLVED`
**Then** the transition shall succeed.

---

## AC-013 — Valid RESOLVED Transition

**Requirement:** REQ-009

**Given** a ticket is `RESOLVED`
**When** it is transitioned to `CLOSED`
**Then** the transition shall succeed.

---

## AC-014 — Valid OPEN Cancellation

**Requirement:** REQ-009

**Given** a ticket is `OPEN`
**When** it is transitioned to `CANCELLED`
**Then** the transition shall succeed.

---

## AC-015 — Valid IN_PROGRESS Cancellation

**Requirement:** REQ-009

**Given** a ticket is `IN_PROGRESS`
**When** it is transitioned to `CANCELLED`
**Then** the transition shall succeed.

---

## AC-016 — Invalid CLOSED Transition

**Requirement:** REQ-009

**Given** a ticket is `CLOSED`
**When** an attempt is made to transition it to `OPEN`
**Then** the backend shall reject the transition.

---

## AC-017 — Invalid RESOLVED Transition

**Requirement:** REQ-009

**Given** a ticket is `RESOLVED`
**When** an attempt is made to transition it to `OPEN`
**Then** the backend shall reject the transition.

---

## AC-018 — Invalid CANCELLED Transition

**Requirement:** REQ-009

**Given** a ticket is `CANCELLED`
**When** an attempt is made to transition it to `OPEN`
**Then** the backend shall reject the transition.

---

## AC-019 — UI Error Handling

**Requirement:** REQ-010

**Given** an API operation fails
**When** the frontend receives the error
**Then** the user shall see a meaningful human-readable message.

---

## AC-020 — No Secrets

**Requirement:** NFR-005

**Given** the repository is reviewed
**When** source files and configuration are inspected
**Then** no secrets or credentials shall be committed.

