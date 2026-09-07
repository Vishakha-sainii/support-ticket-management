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
* list tickets
* get ticket
* update ticket
* add comment
* status transition
* validation errors
* not-found errors
* invalid transitions

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
REQ-004 → Ticket update tests
REQ-009 → Validation/state-machine tests
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

