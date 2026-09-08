# UI Flow Specification

## 0. Authentication

### Login Screen

The application entry point for unauthenticated users shall be a login screen.

Form fields:

```text
Username
Password
```

Actions:

* Submit login
* Display authentication error on failure

### Successful Login

```text
Login → POST /api/auth/login → Success → Store session → Navigate to ticket list
```

On success:

* The application shall know the authenticated username and role (`ADMIN` or `USER`).
* The header shall display the logged-in user and a logout action.

### Invalid Login

```text
Login → POST /api/auth/login → 401 → Display meaningful authentication error
```

### Logout

```text
Logout → POST /api/auth/logout → Clear session → Navigate to login screen
```

### Role-Aware Application State

After login, the frontend shall maintain role-aware state (via `/api/auth/me` on load).

* **ADMIN:** full ticket management UI including Create Ticket
* **USER:** ticket viewing and editing UI without Create Ticket

### Protected Routes

Unauthenticated users attempting to access ticket pages shall be redirected to the login screen.

If a **USER** navigates directly to the create-ticket route, the application shall redirect to the ticket list and display an access-denied message (backend remains authoritative via `403` on `POST /api/tickets`).

---

## 1. Ticket List

The main screen shall display:

* ticket ID
* title
* priority (as a visual badge)
* status (as a visual badge)
* assignee
* creation date

Actions:

* create ticket (**ADMIN only**)
* view ticket
* search
* filter by status

**ADMIN UI:** Create Ticket action visible in toolbar.

**USER UI:** Create Ticket action hidden.

---

## 2. Create Ticket

**ADMIN only.**

User selects:

```text
Create Ticket
```

Form (all fields mandatory, asterisk shown):

```text
Title *
Description *
Priority *
Assignee *
```

Validation:

* Asterisk (`*`) on every mandatory label
* Blank fields cannot be submitted
* Whitespace-only values rejected with field-level messages
* Invalid fields visually indicated

On successful submission:

```text
Create → API → Success → Navigate/show ticket
```

On validation failure:

```text
Create → API or client validation → Validation Error → Display message
```

---

## 3. Ticket Details

The details page shall display:

* title
* description
* priority (badge)
* status (badge)
* assignee
* timestamps
* comments

Actions:

* edit (ADMIN and USER)
* add comment (ADMIN and USER)
* change status (ADMIN and USER)

---

## 4. Edit Ticket

User can update (ADMIN and USER):

* title *
* description *
* priority *
* assignee *

Status shall not be edited through the normal edit form.

Validation requirements match the create form (asterisks, whitespace rejection, field-level errors).

---

## 5. Add Comment

The user enters comment text and submits.

Form:

```text
Comment *
```

Validation:

* Asterisk on mandatory label
* Blank and whitespace-only comment text rejected

Success:

```text
Comment appears in ticket comments.
```

Failure:

```text
Meaningful error is displayed.
```

---

## 6. Search

The user enters a keyword.

The UI calls the ticket search API and displays matching tickets.

Zero results should produce a useful empty-state message.

---

## 7. Status Filter

The UI provides a status selector:

```text
All
OPEN
IN_PROGRESS
RESOLVED
CLOSED
CANCELLED
```

---

## 8. Status Transition

The UI provides only appropriate transition actions where practical.

However, backend validation remains authoritative.

If the backend rejects a transition, the UI must display the returned meaningful error.

The existing state machine and valid transitions are unchanged.

---

## 9. Priority and Status Badges

### Priority Badges

Display priorities as distinct badge/chip components:

| Priority | Styling |
| -------- | ------- |
| LOW | low/neutral |
| MEDIUM | informational |
| HIGH | warning |
| CRITICAL | danger |

Used consistently in: ticket list table, ticket detail header/meta, and anywhere priority is shown.

### Status Badges

Display statuses as distinct badge/chip components:

| Status | Styling |
| ------ | ------- |
| OPEN | informational |
| IN_PROGRESS | active/in-progress |
| RESOLVED | success |
| CLOSED | neutral/completed |
| CANCELLED | danger/cancelled |

Used consistently in: ticket list table, ticket detail header/meta, and status action areas.

Accessibility: labels remain readable; styling does not rely on color alone.

---

## 10. Layout — Full-Screen Responsive

The application shall use a full-width layout that fills the available viewport.

Requirements:

* Remove narrow fixed-width centered container constraint
* Header spans full width
* Ticket table uses available horizontal space
* Reasonable padding and spacing preserved
* No unnecessary horizontal overflow

### Desktop

* Full-width toolbar and table
* Multi-column table layout

### Tablet

* Toolbar controls may wrap
* Table remains readable

### Mobile

* Toolbar stacks vertically
* Table may scroll horizontally if needed
* Forms stack with full-width inputs
* Buttons remain tappable

---

## 11. Error Handling

The UI should display user-friendly messages for:

* authentication errors
* authorization errors (403)
* validation errors
* not found
* invalid state transition
* server errors
* network/backend unavailable

Do not expose raw stack traces.

---

## 12. Loading and Empty States

The UI should provide reasonable feedback while:

* loading tickets
* loading details
* submitting forms
* authenticating

Empty lists should show an appropriate empty state.
