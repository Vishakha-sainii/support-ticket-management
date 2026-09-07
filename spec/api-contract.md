# REST API Contract

Base path:

```text
/api
```

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
  "assignee": "support-user"
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

Success:

`200 OK`

---

# 3. Get Ticket

### GET `/api/tickets/{id}`

Returns a specific ticket including comments.

Success:

`200 OK`

Not found:

`404 Not Found`

---

# 4. Update Ticket

### PUT `/api/tickets/{id}`

Updates editable ticket fields.

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

Success:

`200 OK`

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

---

# 7. Search

Search is supported through:

```text
GET /api/tickets?search=<keyword>
```

Search should match ticket title and description.

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

