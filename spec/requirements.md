# Support Ticket Management System — Requirements Specification

## 1. Purpose

The Support Ticket Management System allows users to create, manage, search, filter, and track support tickets through a controlled lifecycle.

The system consists of a backend REST API, persistent database storage, and a frontend user interface.

The backend is the authoritative source for business rules and state-machine enforcement.

---

## 2. Scope

### In Scope

* Create support tickets
* List support tickets
* View ticket details
* Update ticket title
* Update ticket description
* Update ticket priority
* Update ticket assignee
* Add comments
* Search tickets by keyword
* Filter tickets by status
* Persist ticket data
* Backend input validation
* Consistent API error handling
* Frontend validation/error display
* Ticket lifecycle/state-machine enforcement
* Automated testing
* Authentication and role-based access control
* Mandatory field validation (frontend and backend)
* Priority and status visual styling
* Full-screen responsive UI layout

### Out of Scope

Unless explicitly required later:

* Email notifications
* File attachments
* Real-time notifications
* SLA management
* Escalation workflows
* Multiple organizations/tenants
* Microservices
* Event streaming
* Advanced reporting
* Audit history beyond what is necessary for the assignment

---

# 3. Functional Requirements

## REQ-001 — Create Ticket

The system shall allow an authenticated **ADMIN** user to create a support ticket.

A ticket shall contain at minimum:

* title
* description
* priority
* assignee

A newly created ticket shall start in `OPEN` status.

The backend shall validate the request before persistence.

---

## REQ-002 — List Tickets

The system shall provide a way to retrieve a list of tickets.

The list shall expose sufficient information to identify and understand each ticket, including:

* ticket ID
* title
* priority
* status
* assignee
* creation timestamp

---

## REQ-003 — View Ticket Details

The system shall allow a user to retrieve details of a specific ticket using its ID.

The details shall include:

* ticket information
* current status
* priority
* assignee
* comments
* relevant timestamps

If the ticket does not exist, the backend shall return a meaningful not-found error.

---

## REQ-004 — Update Ticket

The system shall allow updating:

* title
* description
* priority
* assignee

Status changes shall not be performed through the general update operation.

If a `status` field is included in a `PUT /api/tickets/{id}` request, the backend shall reject the request with a meaningful `400` validation or business error.

Status changes shall use the dedicated state-transition operation (`PATCH /api/tickets/{id}/status`).

The backend shall validate update requests.

---

## REQ-005 — Add Comment

The system shall allow a user to add a comment to an existing ticket.

A comment shall contain:

* comment text
* creation timestamp
* associated ticket

The backend shall reject invalid comment requests.

A comment cannot be added to a ticket that does not exist.

---

## REQ-006 — Search Tickets

The system shall allow tickets to be searched using a keyword.

The search shall support matching relevant ticket text, including at minimum:

* title
* description

Search behavior shall be case-insensitive unless explicitly documented otherwise.

---

## REQ-007 — Filter Tickets by Status

The system shall allow tickets to be filtered by status.

Supported statuses:

* OPEN
* IN_PROGRESS
* RESOLVED
* CLOSED
* CANCELLED

---

## REQ-008 — Persistent Storage

Ticket and comment data shall be persisted in a relational database.

Data shall survive application restart.

The preferred database is PostgreSQL.

H2 may be used for automated tests where appropriate.

---

## REQ-009 — Backend Validation

The backend shall validate incoming requests.

Validation shall cover:

* required fields
* empty/blank values
* field length constraints:
  * title: max 200 characters
  * description: max 5000 characters
  * assignee: max 100 characters
  * comment text: max 2000 characters
* valid enum values
* invalid ticket IDs
* invalid status transitions
* rejection of `status` in `PUT /api/tickets/{id}` requests

Validation failures shall return structured and meaningful API errors.

---

## REQ-010 — Frontend Error Handling

The frontend shall display meaningful errors when:

* validation fails
* a ticket does not exist
* an API request fails
* an invalid status transition is attempted
* the backend is unavailable

Technical/internal exception details shall not be exposed unnecessarily to users.

---

## REQ-011 — Authentication

The system shall provide a simple login mechanism so that users can authenticate through the UI.

The system shall support two roles for local evaluation:

* `ADMIN`
* `USER`

The system shall provide development/demo users configured through application configuration (not embedded in business logic):

| Username | Password | Role |
| -------- | -------- | ---- |
| admin | admin123 | ADMIN |
| user | user123 | USER |

Requirements:

* Provide a login screen.
* Successful login shall establish the authenticated user's identity and role.
* Invalid credentials shall be rejected with a meaningful authentication error.
* The frontend shall know the authenticated user's role.
* The authenticated role shall be enforced by the backend.
* Frontend role checks are usability features only and shall not be the sole security mechanism.
* Demo credentials shall be documented in `README.md` for local evaluation only, with a clear statement that they are development/demo credentials and must not be used in production.

---

## REQ-012 — Role-Based Access Control

The system shall enforce role-based authorization on the backend.

### ADMIN permissions

* View ticket list
* View ticket details
* Create tickets
* Update tickets
* View all tickets

### USER permissions

* View ticket list
* View ticket details
* Update tickets
* Cannot create tickets

### Authorization rules

| Operation | ADMIN | USER |
| --------- | ----- | ---- |
| `GET /api/tickets` | allowed | allowed |
| `GET /api/tickets/{id}` | allowed | allowed |
| `POST /api/tickets` | allowed | **forbidden (403)** |
| `PUT /api/tickets/{id}` | allowed | allowed |

A `USER` attempting `POST /api/tickets` shall receive `HTTP 403 Forbidden` with the application's consistent error response format.

Other authenticated ticket operations (status transitions, comments) remain available to both roles unless explicitly restricted in a future requirement.

Unauthenticated access to ticket APIs shall be rejected.

---

## REQ-013 — Mandatory Field Validation

The following fields are mandatory.

### Ticket creation

* title
* description
* priority
* assignee

### Ticket update

* title
* description
* priority
* assignee

### Comment creation

* comment text

### Frontend requirements

* Display an asterisk (`*`) next to every mandatory field label.
* Prevent submission when mandatory fields are blank.
* Reject whitespace-only values.
* Display meaningful validation messages.
* Visually indicate invalid fields where appropriate.
* Validation shall be consistent across create, update, and comment forms.

### Backend requirements

* Mandatory validation shall be enforced by the backend.
* Never rely only on frontend/HTML validation.
* Blank and whitespace-only values shall be rejected.
* Use appropriate Jakarta Bean Validation where applicable.
* Return the existing consistent validation error structure.
* Do not expose stack traces, SQL details, or internal implementation details.

---

## REQ-014 — Priority Visual Styling

Priority values (`LOW`, `MEDIUM`, `HIGH`, `CRITICAL`) shall be displayed as visually distinct badges/chips throughout the application.

Requirements:

* Each priority shall have clearly distinguishable visual styling.
* Styling shall be consistent throughout the application.
* Styling should communicate severity/importance.
* Text must remain readable and accessible.
* Do not rely on color alone where that would hurt accessibility.

Suggested semantic treatment:

* `LOW` → low/neutral styling
* `MEDIUM` → informational styling
* `HIGH` → warning styling
* `CRITICAL` → danger styling

---

## REQ-015 — Status Visual Styling

Status values (`OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`, `CANCELLED`) shall be displayed as visually distinct badges/chips throughout the application.

Requirements:

* Each status shall have clearly distinguishable visual styling.
* Styling shall be consistent throughout the application.
* Text must remain readable and accessible.
* Do not rely on color alone where that would hurt accessibility.

Suggested semantic treatment:

* `OPEN` → informational styling
* `IN_PROGRESS` → active/in-progress styling
* `RESOLVED` → success styling
* `CLOSED` → neutral/completed styling
* `CANCELLED` → danger/cancelled styling

The existing state machine and valid transitions shall not change.

---

## REQ-016 — Full-Screen Responsive UI

The application shall use the available viewport width and provide a full-screen application layout.

Requirements:

* Use available horizontal space.
* Provide a full-width application layout.
* Support responsive desktop, tablet, and mobile layouts.
* The ticket table/list shall use available horizontal space.
* Avoid unnecessary fixed-width containers.
* Maintain reasonable content spacing and readability.
* Preserve existing functionality.
* Avoid unnecessary horizontal overflow.
* Forms shall remain usable on smaller screens.

---

# 4. Non-Functional Requirements

## NFR-001 — Maintainability

The implementation shall follow clean, understandable Java and Spring Boot practices.

## NFR-002 — Separation of Concerns

Controllers shall handle HTTP concerns.

Business logic shall reside in appropriate service/domain components.

Persistence shall be handled through repository/data-access components.

## NFR-003 — Testability

Core business logic shall be independently testable.

## NFR-004 — Consistent API Errors

API errors shall use a consistent response structure.

## NFR-005 — Security

No production secrets, passwords, tokens, or environment-specific credentials shall be committed to source control.

Development/demo authentication credentials may be configured through application configuration and documented in `README.md` for local evaluation only, with an explicit warning that they must not be used in production.

## NFR-006 — Simplicity

The system shall avoid unnecessary infrastructure and abstractions.

---

# 5. Business Rules

## BR-001

Every newly created ticket starts in `OPEN`.

## BR-002

Ticket status can only change through a valid state transition.

## BR-003

Invalid state transitions must be rejected by the backend.

## BR-004

A ticket must exist before it can be updated or commented on.

## BR-005

Required fields cannot be blank.

## BR-006

Status is controlled by the state machine and is not freely editable.

---

# 6. Edge Cases

The implementation must consider:

* blank title
* blank description
* invalid priority
* missing assignee where required
* non-existent ticket ID
* duplicate/invalid requests where applicable
* blank comment
* search with blank keyword (blank or omitted keyword shall not apply a search filter)
* search returning zero results
* filter returning zero results
* invalid state transition
* transition on non-existent ticket
* database failure
* backend unavailable
* malformed request payload
* field values exceeding maximum length
* `status` field supplied in `PUT /api/tickets/{id}` request body
* unauthenticated API access
* invalid login credentials
* `USER` attempting ticket creation (`POST /api/tickets`)
* whitespace-only mandatory field values
* direct navigation to create-ticket page by `USER`

---

# 7. Acceptance Summary

The system is considered complete when all requirements and acceptance criteria are implemented, tested, and verified against the specification.

