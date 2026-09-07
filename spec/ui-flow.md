# UI Flow Specification

## 1. Ticket List

The main screen shall display:

* ticket ID
* title
* priority
* status
* assignee
* creation date

Actions:

* create ticket
* view ticket
* search
* filter by status

---

# 2. Create Ticket

User selects:

```text
Create Ticket
```

Form:

```text
Title
Description
Priority
Assignee
```

On successful submission:

```text
Create → API → Success → Navigate/show ticket
```

On validation failure:

```text
Create → API → Validation Error → Display message
```

---

# 3. Ticket Details

The details page shall display:

* title
* description
* priority
* status
* assignee
* timestamps
* comments

Actions:

* edit
* add comment
* change status

---

# 4. Edit Ticket

User can update:

* title
* description
* priority
* assignee

Status shall not be edited through the normal edit form.

---

# 5. Add Comment

The user enters comment text and submits.

Success:

```text
Comment appears in ticket comments.
```

Failure:

```text
Meaningful error is displayed.
```

---

# 6. Search

The user enters a keyword.

The UI calls the ticket search API and displays matching tickets.

Zero results should produce a useful empty-state message.

---

# 7. Status Filter

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

# 8. Status Transition

The UI provides only appropriate transition actions where practical.

However, backend validation remains authoritative.

If the backend rejects a transition, the UI must display the returned meaningful error.

---

# 9. Error Handling

The UI should display user-friendly messages for:

* validation errors
* not found
* invalid state transition
* server errors
* network/backend unavailable

Do not expose raw stack traces.

---

# 10. Loading and Empty States

The UI should provide reasonable feedback while:

* loading tickets
* loading details
* submitting forms

Empty lists should show an appropriate empty state.

