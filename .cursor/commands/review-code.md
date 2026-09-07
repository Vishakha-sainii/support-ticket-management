# /review-code

Act as a Senior Staff Software Engineer.

Review the current implementation against:

* spec/
* rules/java-springboot.md
* rules/testing.md
* rules/api-standards.md

Review:

## Architecture

* separation of concerns
* dependency direction
* controller/service/repository boundaries
* unnecessary abstractions

## Java/Spring

* Java 21 practices
* dependency injection
* validation
* transaction boundaries
* exception handling
* JPA usage
* query performance

## API

* REST conventions
* status codes
* validation
* error consistency
* API contract compliance

## Business Rules

Especially verify ticket state-machine enforcement.

## Security

Check for:

* secrets
* unsafe input
* SQL injection risks
* exposed internal errors
* insecure configuration

## Tests

Check:

* important business rules
* negative cases
* state transitions
* API behavior
* persistence

Do not modify code during review.

Classify findings:

CRITICAL
HIGH
MEDIUM
LOW
PASS

Provide file/line evidence where possible.

