# Test Strategy

## 1. Objective

Verify that implementation satisfies functional requirements, business rules, API contracts, persistence requirements and state-machine rules.

---

# 2. Unit Tests

Unit tests should cover:

* service business logic
* state transition validation
* validation rules
* mapping logic where significant
* error scenarios

---

# 3. Repository Tests

Repository tests should verify:

* ticket persistence
* comment persistence
* status filtering
* keyword search

---

# 4. Controller/API Tests

Test:

* create ticket
* list tickets (including required list item fields per AC-003)
* get ticket
* update ticket
* add comment
* status transition
* validation errors
* not-found errors (get, update, comment per AC-005, AC-021, AC-022)
* invalid transitions
* rejection of `status` in `PUT /api/tickets/{id}` (AC-023)

---

# 5. State Machine Tests

Mandatory valid transitions:

```text
OPEN → IN_PROGRESS
IN_PROGRESS → RESOLVED
RESOLVED → CLOSED
OPEN → CANCELLED
IN_PROGRESS → CANCELLED
```

Mandatory invalid examples:

```text
CLOSED → OPEN
RESOLVED → OPEN
CANCELLED → OPEN
```

---

# 6. Persistence Tests

Verify that:

1. ticket is saved
2. application/database lifecycle does not lose persisted data
3. comments remain associated with tickets

---

# 7. Validation Tests

Test:

* blank title
* blank description
* invalid priority
* blank comment
* invalid ticket ID
* invalid status
* field values exceeding maximum length (title 200, description 5000, assignee 100, comment text 2000)
* `status` field in `PUT /api/tickets/{id}` request body

---

# 8. Integration Tests

At least one integration-level flow should verify:

```text
Create ticket
→ retrieve ticket
→ update ticket
→ add comment
→ change status
→ retrieve final ticket
```

---

# 9. Requirement Traceability

Tests should reference or be traceable to requirement IDs.

Example:

```text
REQ-001 → Ticket creation tests
REQ-002 → Ticket list field tests (AC-003)
REQ-004 → Ticket update and PUT status rejection tests (AC-021, AC-023)
REQ-005 → Comment and not-found comment tests (AC-022)
REQ-009 → Validation/state-machine tests
BR-004 → Not-found update/comment tests
BR-006 → Status update rejection tests
NFR-004 → Consistent API error format tests
NFR-005 → Security/no-secrets verification (AC-020)
```

---

# 10. Test Quality

Tests should:

* verify behavior rather than implementation details
* include positive and negative cases
* be deterministic
* avoid unnecessary mocking
* remain maintainable
* not weaken production requirements to make tests pass

---

# 11. Authentication Tests

Test:

* valid ADMIN login (`admin` / `admin123`)
* valid USER login (`user` / `user123`)
* invalid credentials rejected with meaningful `401` error
* unauthenticated access to ticket APIs returns `401`
* session established on successful login
* `GET /api/auth/me` returns current user and role
* logout invalidates session

---

# 12. Authorization Tests

Test:

* ADMIN allowed operations:
  * `GET /api/tickets`
  * `GET /api/tickets/{id}`
  * `POST /api/tickets`
  * `PUT /api/tickets/{id}`
* USER allowed operations:
  * `GET /api/tickets`
  * `GET /api/tickets/{id}`
  * `PUT /api/tickets/{id}`
* USER forbidden operation:
  * `POST /api/tickets` returns `HTTP 403 Forbidden` with consistent error format
* backend authorization verified independently of frontend (use `@WithMockUser`, session-based API tests, or equivalent)

Existing state-machine tests must remain intact.

---

# 13. Mandatory Field Validation Tests

### Backend

Test:

* missing title on create
* missing description on create
* missing priority on create
* missing assignee on create
* missing title on update
* missing description on update
* missing priority on update
* missing assignee on update
* missing comment text
* whitespace-only title
* whitespace-only description
* whitespace-only assignee
* whitespace-only comment text

### Frontend

Test:

* required field asterisk (`*`) displayed on mandatory labels
* blank mandatory fields prevent submission
* whitespace-only values rejected with meaningful messages
* invalid fields visually indicated
* consistent validation across create, update, and comment forms

---

# 14. UI Enhancement Tests

Test where practical:

* role-based Create Ticket visibility (ADMIN visible, USER hidden)
* priority badge rendering for all priority values
* status badge rendering for all status values
* full-width layout (no narrow centered constraint)
* responsive behavior smoke tests (toolbar wrapping, form usability)

---

# 15. Requirement Traceability (Extended)

Example:

```text
REQ-011 → Authentication tests (AC-024–AC-026, AC-048)
REQ-012 → Authorization tests (AC-027–AC-035)
REQ-013 → Mandatory field validation tests (AC-036–AC-041)
REQ-014 → Priority badge tests (AC-042)
REQ-015 → Status badge tests (AC-043)
REQ-016 → Layout/responsive tests (AC-044–AC-047)
```

Existing traceability for REQ-001–REQ-010, BR-001–BR-006, and NFR-001–NFR-006 shall be preserved.

