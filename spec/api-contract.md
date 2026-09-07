# REST API Contract

Base path:

```text
/api
```

Identifiers (`id`, `ticketId`) are `Long` values.

---

# Field Constraints

| Field       | Max Length | Notes                |
| ----------- | ---------- | -------------------- |
| title       | 200        | Required, not blank  |
| description | 5000       | Required, not blank  |
| assignee    | 100        | Required, not blank  |
| comment text | 2000       | Required, not blank  |

Requests exceeding these limits shall be rejected with a `400` validation error.

---

# 1. Create Ticket

### POST `/api/tickets`

Creates a new ticket.

### Request

```json
{
  "title": "Unable to login",
  "description": "User cannot login to the application",
  "priority": "HIGH",
  "assignee": "support-user"
}
```

### Success

`201 Created`

```json
{
  "id": 1,
  "title": "Unable to login",
  "description": "User cannot login to the application",
  "priority": "HIGH",
  "status": "OPEN",
  "assignee": "support-user",
  "createdAt": "2026-01-01T10:00:00Z",
  "updatedAt": "2026-01-01T10:00:00Z"
}
```

---

# 2. List Tickets

### GET `/api/tickets`

Returns tickets.

Optional query parameters:

```text
status
search
```

Example:

```text
GET /api/tickets?status=OPEN
```

Example:

```text
GET /api/tickets?search=login
```

Example:

```text
GET /api/tickets?search=login&status=OPEN
```

Success:

`200 OK`

```json
[
  {
    "id": 1,
    "title": "Unable to login",
    "priority": "HIGH",
    "status": "OPEN",
    "assignee": "support-user",
    "createdAt": "2026-01-01T10:00:00Z"
  }
]
```

List items expose: `id`, `title`, `priority`, `status`, `assignee`, `createdAt`.

When both `search` and `status` are provided, both filters apply.

---

# 3. Get Ticket

### GET `/api/tickets/{id}`

Returns a specific ticket including comments.

Success:

`200 OK`

```json
{
  "id": 1,
  "title": "Unable to login",
  "description": "User cannot login to the application",
  "priority": "HIGH",
  "status": "OPEN",
  "assignee": "support-user",
  "createdAt": "2026-01-01T10:00:00Z",
  "updatedAt": "2026-01-01T10:00:00Z",
  "comments": [
    {
      "id": 10,
      "text": "Investigating the issue.",
      "createdAt": "2026-01-01T11:00:00Z"
    }
  ]
}
```

Not found:

`404 Not Found`

---

# 4. Update Ticket

### PUT `/api/tickets/{id}`

Updates editable ticket fields: `title`, `description`, `priority`, `assignee`.

Request:

```json
{
  "title": "Updated title",
  "description": "Updated description",
  "priority": "MEDIUM",
  "assignee": "new-assignee"
}
```

Status must not be changed through this endpoint.

If the request body includes a `status` field, the backend shall reject the request with `400 Bad Request` and a meaningful validation or business error. Status changes are permitted only through `PATCH /api/tickets/{id}/status`.

Example rejection:

```json
{
  "timestamp": "2026-01-01T10:00:00Z",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Status cannot be changed through ticket update; use PATCH /api/tickets/{id}/status",
  "path": "/api/tickets/1"
}
```

Success:

`200 OK`

Not found:

`404 Not Found`

---

# 5. Change Status

### PATCH `/api/tickets/{id}/status`

Request:

```json
{
  "status": "IN_PROGRESS"
}
```

The backend must validate the transition.

Valid transition:

```text
OPEN → IN_PROGRESS
```

Invalid transition:

```text
CLOSED → OPEN
```

Invalid transitions return a meaningful client error.

Recommended:

`400 Bad Request`

Example:

```json
{
  "timestamp": "2026-01-01T10:00:00Z",
  "status": 400,
  "error": "INVALID_STATE_TRANSITION",
  "message": "Cannot transition from CLOSED to OPEN",
  "path": "/api/tickets/1/status"
}
```

or another documented 4xx response consistent with the final implementation.

---

# 6. Add Comment

### POST `/api/tickets/{id}/comments`

Request:

```json
{
  "text": "Investigating the issue."
}
```

Success:

`201 Created`

```json
{
  "id": 10,
  "ticketId": 1,
  "text": "Investigating the issue.",
  "createdAt": "2026-01-01T11:00:00Z"
}
```

Not found:

`404 Not Found`

---

# 7. Search

Search is supported through:

```text
GET /api/tickets?search=<keyword>
```

Search should match ticket title and description.

Search is case-insensitive.

A blank or omitted `search` parameter shall not apply a search filter.

---

# 8. Status Filter

Filtering is supported through:

```text
GET /api/tickets?status=<STATUS>
```

---

# 9. Validation Error

Example:

```json
{
  "timestamp": "2026-01-01T10:00:00Z",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Title must not be blank",
  "path": "/api/tickets"
}
```

---

# 10. Not Found Error

Example:

```json
{
  "timestamp": "2026-01-01T10:00:00Z",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Ticket not found",
  "path": "/api/tickets/999"
}
```

---

# 11. API Principles

* Use appropriate HTTP methods.
* Use appropriate HTTP status codes.
* Validate requests at the backend.
* Do not expose stack traces.
* Return consistent errors.
* Do not expose database implementation details.
* Keep request/response DTOs separate from persistence entities where appropriate.

