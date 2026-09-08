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
**Then** the system shall return the tickets successfully
**And** each list item shall include `id`, `title`, `priority`, `status`, `assignee`, and `createdAt`.

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

**Requirement:** REQ-009

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

---

## AC-021 — Update Ticket Not Found

**Requirement:** REQ-004, BR-004

**Given** a ticket ID does not exist
**When** an update request is submitted for that ticket
**Then** the backend shall return a not-found response with a meaningful error.

---

## AC-022 — Comment on Missing Ticket

**Requirement:** REQ-005, BR-004

**Given** a ticket ID does not exist
**When** a comment is submitted for that ticket
**Then** the backend shall return a not-found response with a meaningful error.

---

## AC-023 — Reject Status in Update

**Requirement:** REQ-004, REQ-009, BR-006

**Given** an existing ticket
**When** a `PUT /api/tickets/{id}` request includes a `status` field
**Then** the backend shall reject the request with a `400` validation or business error
**And** the ticket status shall remain unchanged.

---

## AC-024 — ADMIN Login

**Requirement:** REQ-011

**Given** valid ADMIN credentials
**When** the user submits the login form
**Then** authentication shall succeed
**And** the application shall establish the authenticated ADMIN identity and role.

---

## AC-025 — USER Login

**Requirement:** REQ-011

**Given** valid USER credentials
**When** the user submits the login form
**Then** authentication shall succeed
**And** the application shall establish the authenticated USER identity and role.

---

## AC-026 — Invalid Login

**Requirement:** REQ-011

**Given** invalid credentials
**When** the user submits the login form
**Then** authentication shall be rejected
**And** a meaningful authentication error shall be displayed.

---

## AC-027 — ADMIN View Tickets

**Requirement:** REQ-012

**Given** an authenticated ADMIN
**When** the user requests the ticket list
**Then** tickets shall be displayed successfully.

---

## AC-028 — ADMIN View Ticket Details

**Requirement:** REQ-012

**Given** an authenticated ADMIN and an existing ticket
**When** the user opens the ticket details
**Then** the ticket details shall be displayed.

---

## AC-029 — ADMIN Create Ticket

**Requirement:** REQ-001, REQ-012

**Given** an authenticated ADMIN
**When** valid ticket information is submitted
**Then** the ticket shall be created successfully.

---

## AC-030 — ADMIN Update Ticket

**Requirement:** REQ-004, REQ-012

**Given** an authenticated ADMIN and an existing ticket
**When** valid update information is submitted
**Then** the changes shall be persisted.

---

## AC-031 — USER View Tickets

**Requirement:** REQ-012

**Given** an authenticated USER
**When** the user requests the ticket list
**Then** tickets shall be displayed successfully.

---

## AC-032 — USER View Ticket Details

**Requirement:** REQ-012

**Given** an authenticated USER and an existing ticket
**When** the user opens the ticket details
**Then** the ticket details shall be displayed.

---

## AC-033 — USER Update Ticket

**Requirement:** REQ-004, REQ-012

**Given** an authenticated USER and an existing ticket
**When** valid update information is submitted
**Then** the changes shall be persisted.

---

## AC-034 — USER Create Ticket UI Hidden

**Requirement:** REQ-012

**Given** an authenticated USER
**When** the ticket list is displayed
**Then** the Create Ticket action shall not be visible.

---

## AC-035 — USER Forbidden Create Ticket API

**Requirement:** REQ-012

**Given** an authenticated USER
**When** a `POST /api/tickets` request is submitted
**Then** the backend shall return `HTTP 403 Forbidden`
**And** the response shall use the consistent error format.

---

## AC-036 — Required Field Indicators

**Requirement:** REQ-013

**Given** a ticket create, update, or comment form
**When** the form is displayed
**Then** every mandatory field label shall display an asterisk (`*`).

---

## AC-037 — Frontend Blank Required Field Prevention

**Requirement:** REQ-013

**Given** a ticket or comment form with blank mandatory fields
**When** the user attempts to submit
**Then** submission shall be prevented
**And** meaningful validation messages shall be displayed.

---

## AC-038 — Whitespace-Only Rejection (Frontend)

**Requirement:** REQ-013

**Given** a mandatory field containing only whitespace
**When** the user attempts to submit
**Then** submission shall be prevented
**And** a meaningful validation message shall be displayed.

---

## AC-039 — Backend Missing Required Fields

**Requirement:** REQ-009, REQ-013

**Given** a request with missing required fields
**When** the backend processes the request
**Then** the request shall be rejected with a meaningful validation error.

---

## AC-040 — Backend Whitespace-Only Rejection

**Requirement:** REQ-009, REQ-013

**Given** a request with whitespace-only required field values
**When** the backend processes the request
**Then** the request shall be rejected with a meaningful validation error.

---

## AC-041 — Validation Error Display

**Requirement:** REQ-010, REQ-013

**Given** a validation failure
**When** the frontend receives or detects the error
**Then** a meaningful human-readable message shall be displayed
**And** invalid fields shall be visually indicated where appropriate.

---

## AC-042 — Priority Badge Styling

**Requirement:** REQ-014

**Given** a ticket with a priority value
**When** the priority is displayed in the UI
**Then** it shall appear as a visually distinct badge/chip consistent throughout the application.

---

## AC-043 — Status Badge Styling

**Requirement:** REQ-015

**Given** a ticket with a status value
**When** the status is displayed in the UI
**Then** it shall appear as a visually distinct badge/chip consistent throughout the application.

---

## AC-044 — Full-Width Layout

**Requirement:** REQ-016

**Given** the application is loaded on a desktop viewport
**When** the user views any page
**Then** the layout shall use the available viewport width
**And** shall not be constrained to a narrow centered column.

---

## AC-045 — Responsive Tablet Layout

**Requirement:** REQ-016

**Given** the application is loaded on a tablet viewport
**When** the user views ticket list or forms
**Then** the layout shall remain usable and readable.

---

## AC-046 — Responsive Mobile Layout

**Requirement:** REQ-016

**Given** the application is loaded on a mobile viewport
**When** the user views ticket list or forms
**Then** the layout shall remain usable without unnecessary horizontal overflow.

---

## AC-047 — Existing Functionality Preserved

**Requirement:** REQ-016

**Given** the layout and UI enhancements are applied
**When** existing ticket operations are performed
**Then** all previously supported functionality shall remain intact.

---

## AC-048 — Unauthenticated Access Rejected

**Requirement:** REQ-011, REQ-012

**Given** an unauthenticated client
**When** a protected ticket API is called
**Then** the backend shall reject the request with an appropriate authentication error.
