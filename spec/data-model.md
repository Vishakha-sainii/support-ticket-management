# Data Model Specification

## 1. Ticket

Represents a support request.

### Fields

| Field       | Type      | Required        | Description              |
| ----------- | --------- | --------------- | ------------------------ |
| id          | Long/UUID | Yes             | Unique ticket identifier |
| title       | String    | Yes             | Ticket title             |
| description | String    | Yes             | Ticket description       |
| priority    | Priority  | Yes             | Ticket priority          |
| status      | Status    | Yes             | Current lifecycle status |
| assignee    | String    | Yes/Conditional | Assigned user/person     |
| createdAt   | Timestamp | Yes             | Creation timestamp       |
| updatedAt   | Timestamp | Yes             | Last update timestamp    |

The exact ID strategy may be selected during implementation provided the API contract remains consistent.

---

# 2. Comment

Represents a comment associated with a ticket.

| Field     | Type      | Required |
| --------- | --------- | -------- |
| id        | Long/UUID | Yes      |
| ticketId  | Long/UUID | Yes      |
| text      | String    | Yes      |
| createdAt | Timestamp | Yes      |

Relationship:

```text
Ticket 1 ─────── * Comment
```

A ticket may have zero or more comments.

---

# 3. Priority

Supported values:

```text
LOW
MEDIUM
HIGH
CRITICAL
```

The implementation may use a different priority set only if required by the assignment specification; otherwise use the above standard set.

---

# 4. Status

Supported values:

```text
OPEN
IN_PROGRESS
RESOLVED
CLOSED
CANCELLED
```

---

# 5. Constraints

Ticket:

* title must not be blank
* description must not be blank
* priority must be valid
* status must be valid
* assignee must satisfy the defined validation rules

Comment:

* text must not be blank
* comment must reference an existing ticket

---

# 6. Indexing

Indexes should be considered for:

* ticket status
* ticket creation date
* search fields where appropriate

Indexes should only be added when justified by the implemented query patterns.

---

# 7. Persistence

Tickets and comments must be stored persistently.

Deleting or updating a ticket must preserve referential integrity for comments according to the chosen relationship strategy.

