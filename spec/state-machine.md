# Ticket State Machine

## 1. Supported States

```text
OPEN
IN_PROGRESS
RESOLVED
CLOSED
CANCELLED
```

---

# 2. State Transition Rules

| Current State | Allowed Next State |
| ------------- | ------------------ |
| OPEN          | IN_PROGRESS        |
| OPEN          | CANCELLED          |
| IN_PROGRESS   | RESOLVED           |
| IN_PROGRESS   | CANCELLED          |
| RESOLVED      | CLOSED             |
| CLOSED        | None               |
| CANCELLED     | None               |

---

# 3. State Diagram

```text
             ┌──────────────┐
             │     OPEN     │
             └──────┬───────┘
                    │
          ┌─────────┴─────────┐
          ↓                   ↓
   IN_PROGRESS           CANCELLED
          │
          ↓
      RESOLVED
          │
          ↓
       CLOSED
```

---

# 4. Valid Transitions

### OPEN → IN_PROGRESS

Allowed.

### IN_PROGRESS → RESOLVED

Allowed.

### RESOLVED → CLOSED

Allowed.

### OPEN → CANCELLED

Allowed.

### IN_PROGRESS → CANCELLED

Allowed.

---

# 5. Invalid Transitions

The backend must reject invalid transitions.

Examples:

```text
CLOSED → OPEN
CLOSED → IN_PROGRESS
CLOSED → RESOLVED
CANCELLED → OPEN
CANCELLED → IN_PROGRESS
CANCELLED → RESOLVED
RESOLVED → OPEN
RESOLVED → IN_PROGRESS
OPEN → RESOLVED
OPEN → CLOSED
IN_PROGRESS → OPEN
IN_PROGRESS → CLOSED
```

The implementation should reject any transition not explicitly listed as valid.

---

# 6. Enforcement

The frontend may hide invalid actions for better UX, but this is not sufficient.

The backend must independently enforce the state machine.

A client must never be able to bypass the state machine by directly calling the REST API.

---

# 7. Error Behavior

For an invalid transition:

* ticket state must remain unchanged
* no partial update should be persisted
* API should return a meaningful 4xx error
* frontend should display the error

---

# 8. Testing Requirements

Every valid transition must have a positive test.

Every important invalid transition must have a negative test.

At minimum test:

```text
OPEN → IN_PROGRESS       PASS
IN_PROGRESS → RESOLVED   PASS
RESOLVED → CLOSED        PASS
OPEN → CANCELLED         PASS
IN_PROGRESS → CANCELLED  PASS

CLOSED → OPEN            FAIL
RESOLVED → OPEN          FAIL
CANCELLED → OPEN         FAIL
```

