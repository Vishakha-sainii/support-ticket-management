# Backend API — cURL Examples

Copy-paste examples for testing the Support Ticket Management REST API.

**Base URL (default):** `http://localhost:8080`

Set these once in your terminal for shorter commands:

```bash
export BASE_URL=http://localhost:8080
export COOKIE_JAR=/tmp/support-ticket-cookies.txt
```

Start the backend before running these:

```bash
cd backend
# load DATABASE_* env vars, then:
./gradlew bootRun
```

> **Authentication:** All ticket endpoints require an authenticated session. Use the login examples below first, then pass `-b "$COOKIE_JAR"` on subsequent requests. The UI uses the same session cookie mechanism.

> **Demo credentials (development only — not for production):**
> - ADMIN: `admin` / `admin123`
> - USER: `user` / `user123`

For formatted JSON output, pipe through `jq` if installed: `| jq`

---

## Health Check (development)

Not part of the formal API contract; useful to verify the server is running. Does **not** require authentication.

```bash
curl -s "$BASE_URL/api/health"
```

**Expected:** `200 OK`

```json
{"status":"UP"}
```

---

## Authentication

### Login as ADMIN

```bash
rm -f "$COOKIE_JAR"

curl -s -c "$COOKIE_JAR" -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

**Expected:** `200 OK`

```json
{
  "username": "admin",
  "role": "ADMIN"
}
```

### Login as USER

```bash
rm -f "$COOKIE_JAR"

curl -s -c "$COOKIE_JAR" -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user",
    "password": "user123"
  }'
```

**Expected:** `200 OK`

```json
{
  "username": "user",
  "role": "USER"
}
```

### Get current user

```bash
curl -s -b "$COOKIE_JAR" "$BASE_URL/api/auth/me"
```

**Expected:** `200 OK` — returns `username` and `role`

### Invalid credentials

```bash
curl -s -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "nobody",
    "password": "wrong-password"
  }'
```

**Expected:** `401 Unauthorized`, `error: "AUTHENTICATION_ERROR"`

### Unauthenticated ticket access

```bash
rm -f "$COOKIE_JAR"
curl -s "$BASE_URL/api/tickets"
```

**Expected:** `401 Unauthorized`, `error: "AUTHENTICATION_ERROR"`

### Logout

```bash
curl -s -b "$COOKIE_JAR" -c "$COOKIE_JAR" -X POST "$BASE_URL/api/auth/logout"
```

**Expected:** `204 No Content`

---

## 1. Create Ticket

**POST** `/api/tickets` — creates a ticket with status `OPEN`. **Requires ADMIN role.**

Log in as ADMIN first (see [Authentication](#authentication)), then:

```bash
curl -s -b "$COOKIE_JAR" -X POST "$BASE_URL/api/tickets" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Unable to login",
    "description": "User cannot login to the application",
    "priority": "HIGH",
    "assignee": "support-user"
  }'
```

**Expected:** `201 Created`

**Priority values:** `LOW`, `MEDIUM`, `HIGH`, `CRITICAL`

Save the returned `id` for later commands:

```bash
export TICKET_ID=1
```

### Forbidden: USER cannot create tickets

Log in as USER, then:

```bash
curl -s -b "$COOKIE_JAR" -X POST "$BASE_URL/api/tickets" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Forbidden ticket",
    "description": "Should not be created",
    "priority": "HIGH",
    "assignee": "support-user"
  }'
```

**Expected:** `403 Forbidden`, `error: "FORBIDDEN"`

### Validation error (blank title)

```bash
curl -s -b "$COOKIE_JAR" -X POST "$BASE_URL/api/tickets" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "",
    "description": "Description",
    "priority": "HIGH",
    "assignee": "support-user"
  }'
```

**Expected:** `400 Bad Request`, `error: "VALIDATION_ERROR"`

### Validation error (whitespace-only title)

```bash
curl -s -b "$COOKIE_JAR" -X POST "$BASE_URL/api/tickets" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "   ",
    "description": "Description",
    "priority": "HIGH",
    "assignee": "support-user"
  }'
```

**Expected:** `400 Bad Request`, `error: "VALIDATION_ERROR"`

---

## 2. List Tickets

**GET** `/api/tickets` — requires authentication (ADMIN or USER).

### All tickets

```bash
curl -s -b "$COOKIE_JAR" "$BASE_URL/api/tickets"
```

**Expected:** `200 OK` — JSON array of ticket summaries

### Filter by status

```bash
curl -s -b "$COOKIE_JAR" "$BASE_URL/api/tickets?status=OPEN"
```

**Status values:** `OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`, `CANCELLED`

### Search by keyword (title or description, case-insensitive)

```bash
curl -s -b "$COOKIE_JAR" "$BASE_URL/api/tickets?search=login"
```

### Combined search + status filter

```bash
curl -s -b "$COOKIE_JAR" "$BASE_URL/api/tickets?search=login&status=OPEN"
```

### Invalid status filter

```bash
curl -s -b "$COOKIE_JAR" "$BASE_URL/api/tickets?status=INVALID"
```

**Expected:** `400 Bad Request`, `error: "VALIDATION_ERROR"`

---

## 3. Get Ticket by ID

**GET** `/api/tickets/{id}` — returns ticket details including comments.

```bash
curl -s -b "$COOKIE_JAR" "$BASE_URL/api/tickets/$TICKET_ID"
```

**Expected:** `200 OK`

### Ticket not found

```bash
curl -s -b "$COOKIE_JAR" "$BASE_URL/api/tickets/999"
```

**Expected:** `404 Not Found`, `error: "NOT_FOUND"`

---

## 4. Update Ticket

**PUT** `/api/tickets/{id}` — updates `title`, `description`, `priority`, `assignee` only. Requires authentication (ADMIN or USER).

**Do not** include `status` in the body; use PATCH `/status` instead.

```bash
curl -s -b "$COOKIE_JAR" -X PUT "$BASE_URL/api/tickets/$TICKET_ID" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated title",
    "description": "Updated description",
    "priority": "MEDIUM",
    "assignee": "new-assignee"
  }'
```

**Expected:** `200 OK`

### Rejected: status field in PUT body

```bash
curl -s -b "$COOKIE_JAR" -X PUT "$BASE_URL/api/tickets/$TICKET_ID" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated title",
    "description": "Updated description",
    "priority": "MEDIUM",
    "assignee": "new-assignee",
    "status": "CLOSED"
  }'
```

**Expected:** `400 Bad Request`, `error: "VALIDATION_ERROR"`

### Update non-existent ticket

```bash
curl -s -b "$COOKIE_JAR" -X PUT "$BASE_URL/api/tickets/999" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Title",
    "description": "Description",
    "priority": "HIGH",
    "assignee": "support-user"
  }'
```

**Expected:** `404 Not Found`

---

## 5. Change Ticket Status

**PATCH** `/api/tickets/{id}/status` — state machine transition. Requires authentication.

### OPEN → IN_PROGRESS

```bash
curl -s -b "$COOKIE_JAR" -X PATCH "$BASE_URL/api/tickets/$TICKET_ID/status" \
  -H "Content-Type: application/json" \
  -d '{"status": "IN_PROGRESS"}'
```

**Expected:** `200 OK`

### Valid transitions (reference)

| From | To |
|------|-----|
| `OPEN` | `IN_PROGRESS`, `CANCELLED` |
| `IN_PROGRESS` | `RESOLVED`, `CANCELLED` |
| `RESOLVED` | `CLOSED` |
| `CLOSED` | *(none — terminal)* |
| `CANCELLED` | *(none — terminal)* |

### IN_PROGRESS → RESOLVED

```bash
curl -s -b "$COOKIE_JAR" -X PATCH "$BASE_URL/api/tickets/$TICKET_ID/status" \
  -H "Content-Type: application/json" \
  -d '{"status": "RESOLVED"}'
```

### RESOLVED → CLOSED

```bash
curl -s -b "$COOKIE_JAR" -X PATCH "$BASE_URL/api/tickets/$TICKET_ID/status" \
  -H "Content-Type: application/json" \
  -d '{"status": "CLOSED"}'
```

### OPEN → CANCELLED

```bash
curl -s -b "$COOKIE_JAR" -X PATCH "$BASE_URL/api/tickets/$TICKET_ID/status" \
  -H "Content-Type: application/json" \
  -d '{"status": "CANCELLED"}'
```

### Invalid transition (e.g. CLOSED → OPEN)

```bash
curl -s -b "$COOKIE_JAR" -X PATCH "$BASE_URL/api/tickets/$TICKET_ID/status" \
  -H "Content-Type: application/json" \
  -d '{"status": "OPEN"}'
```

**Expected:** `400 Bad Request`, `error: "INVALID_STATE_TRANSITION"`

---

## 6. Add Comment

**POST** `/api/tickets/{id}/comments` — requires authentication.

```bash
curl -s -b "$COOKIE_JAR" -X POST "$BASE_URL/api/tickets/$TICKET_ID/comments" \
  -H "Content-Type: application/json" \
  -d '{"text": "Investigating the issue."}'
```

**Expected:** `201 Created`

### Comment on non-existent ticket

```bash
curl -s -b "$COOKIE_JAR" -X POST "$BASE_URL/api/tickets/999/comments" \
  -H "Content-Type: application/json" \
  -d '{"text": "Investigating the issue."}'
```

**Expected:** `404 Not Found`

### Blank comment text

```bash
curl -s -b "$COOKIE_JAR" -X POST "$BASE_URL/api/tickets/$TICKET_ID/comments" \
  -H "Content-Type: application/json" \
  -d '{"text": ""}'
```

**Expected:** `400 Bad Request`, `error: "VALIDATION_ERROR"`

### Whitespace-only comment text

```bash
curl -s -b "$COOKIE_JAR" -X POST "$BASE_URL/api/tickets/$TICKET_ID/comments" \
  -H "Content-Type: application/json" \
  -d '{"text": "   "}'
```

**Expected:** `400 Bad Request`, `error: "VALIDATION_ERROR"`

---

## Full Lifecycle Example

Run these in order to exercise login → create → read → update → comment → status change → final read:

```bash
export BASE_URL=http://localhost:8080
export COOKIE_JAR=/tmp/support-ticket-cookies.txt
rm -f "$COOKIE_JAR"

# 0. Login as ADMIN
curl -s -c "$COOKIE_JAR" -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}'

# 1. Create
CREATE_RESPONSE=$(curl -s -b "$COOKIE_JAR" -c "$COOKIE_JAR" -X POST "$BASE_URL/api/tickets" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Lifecycle ticket",
    "description": "End-to-end curl test",
    "priority": "HIGH",
    "assignee": "support-user"
  }')
echo "$CREATE_RESPONSE"

# 2. Extract ID (requires jq)
export TICKET_ID=$(echo "$CREATE_RESPONSE" | jq -r '.id')
echo "TICKET_ID=$TICKET_ID"

# 3. Get details
curl -s -b "$COOKIE_JAR" "$BASE_URL/api/tickets/$TICKET_ID"

# 4. Update
curl -s -b "$COOKIE_JAR" -X PUT "$BASE_URL/api/tickets/$TICKET_ID" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated lifecycle ticket",
    "description": "Updated description",
    "priority": "MEDIUM",
    "assignee": "new-assignee"
  }'

# 5. Add comment
curl -s -b "$COOKIE_JAR" -X POST "$BASE_URL/api/tickets/$TICKET_ID/comments" \
  -H "Content-Type: application/json" \
  -d '{"text": "Working on it."}'

# 6. Transition OPEN → IN_PROGRESS
curl -s -b "$COOKIE_JAR" -X PATCH "$BASE_URL/api/tickets/$TICKET_ID/status" \
  -H "Content-Type: application/json" \
  -d '{"status": "IN_PROGRESS"}'

# 7. Final read
curl -s -b "$COOKIE_JAR" "$BASE_URL/api/tickets/$TICKET_ID"

# 8. Logout
curl -s -b "$COOKIE_JAR" -c "$COOKIE_JAR" -X POST "$BASE_URL/api/auth/logout"
```

---

## Error Response Format

All API errors use a consistent JSON shape:

```json
{
  "timestamp": "2026-01-01T10:00:00Z",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Human-readable message",
  "path": "/api/tickets"
}
```

| `error` value | Typical HTTP status |
|---------------|---------------------|
| `AUTHENTICATION_ERROR` | 401 |
| `FORBIDDEN` | 403 |
| `VALIDATION_ERROR` | 400 |
| `INVALID_STATE_TRANSITION` | 400 |
| `NOT_FOUND` | 404 |

---

## Field Limits

| Field | Max length |
|-------|------------|
| `title` | 200 |
| `description` | 5000 |
| `assignee` | 100 |
| comment `text` | 2000 |

All listed fields are mandatory and must not be blank or whitespace-only.

---

## Related Documentation

- [spec/api-contract.md](../spec/api-contract.md) — authoritative API specification
- [spec/state-machine.md](../spec/state-machine.md) — status transition rules
- [README.md](../README.md) — setup, demo authentication, and run instructions
