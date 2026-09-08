# Data Model Specification

## 1. Ticket

Represents a support request.

### Fields

| Field       | Type      | Required | Max Length | Description              |
| ----------- | --------- | -------- | ---------- | ------------------------ |
| id          | Long      | Yes      | —          | Unique ticket identifier |
| title       | String    | Yes      | 200        | Ticket title             |
| description | String    | Yes      | 5000       | Ticket description       |
| priority    | Priority  | Yes      | —          | Ticket priority          |
| status      | Status    | Yes      | —          | Current lifecycle status |
| assignee    | String    | Yes      | 100        | Assigned user/person     |
| createdAt   | Timestamp | Yes      | —          | Creation timestamp       |
| updatedAt   | Timestamp | Yes      | —          | Last update timestamp    |

Ticket and comment identifiers use `Long` with database-generated values.

---

# 2. Comment

Represents a comment associated with a ticket.

| Field     | Type      | Required | Max Length |
| --------- | --------- | -------- | ---------- |
| id        | Long      | Yes      | —          |
| ticketId  | Long      | Yes      | —          |
| text      | String    | Yes      | 2000       |
| createdAt | Timestamp | Yes      | —          |

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

* title must not be blank or whitespace-only and must not exceed 200 characters
* description must not be blank or whitespace-only and must not exceed 5000 characters
* priority must be valid and present
* status must be valid
* assignee must not be blank or whitespace-only and must not exceed 100 characters

Comment:

* text must not be blank or whitespace-only and must not exceed 2000 characters
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

