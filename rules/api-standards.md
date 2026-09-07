# REST API Standards

## Naming

Use resource-oriented URLs.

Preferred:

```text
/api/tickets
/api/tickets/{id}
/api/tickets/{id}/comments
```

Avoid action-oriented URLs unless necessary.

## HTTP Methods

Use:

```text
GET     Read
POST    Create
PUT     Full/update representation
PATCH   Partial update/state transition
DELETE  Delete
```

## Status Codes

Preferred:

```text
200 OK
201 Created
204 No Content
400 Bad Request
404 Not Found
409 Conflict
500 Internal Server Error
```

Use the most appropriate code for the actual situation.

## Validation

Invalid requests must return 4xx responses.

Validation messages must be meaningful.

## Errors

Use a consistent error format.

Do not expose:

* stack traces
* SQL
* internal class names
* database credentials
* implementation details

## Pagination

If pagination is introduced, use a consistent contract.

Do not add pagination complexity unless required by the assignment.

## Search

Search/filter parameters should be represented as query parameters.

Example:

```text
GET /api/tickets?search=login&status=OPEN
```

## API Compatibility

Do not change an approved API contract without updating:

1. specification
2. implementation
3. tests
4. documentation

## Backend Authority

Frontend restrictions must never replace backend business-rule validation.

