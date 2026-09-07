# Testing Rules

## General

Tests must verify business behavior and requirements.

Do not write tests only to increase code coverage.

## Naming

Test names should clearly describe:

```text
given_when_then
```

or an equally descriptive convention.

## Unit Tests

Use unit tests for:

* business logic
* state transitions
* validation rules
* calculations/mappings

Mock only external dependencies where useful.

Do not mock the class under test.

## Integration Tests

Use integration tests for:

* persistence
* repository queries
* API/database integration
* transaction behavior

## API Tests

Verify:

* HTTP method
* URL
* request
* response
* status code
* validation
* error behavior

## Negative Tests

Every important business rule must have failure tests.

Particularly test:

* missing fields
* invalid IDs
* invalid enums
* invalid state transitions

## State Machine

Every allowed transition should have a positive test.

Representative invalid transitions must have negative tests.

## Persistence

Verify that successful operations are actually persisted.

## Test Independence

Tests should not depend on execution order.

Tests should clean up or isolate data appropriately.

## Quality

Never:

* remove tests to make implementation pass
* weaken assertions without justification
* change requirements simply because implementation is difficult
* create meaningless tests

