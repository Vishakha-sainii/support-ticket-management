# Java Spring Boot Engineering Rules

## Java

* Use Java 21.
* Prefer clear, readable modern Java.
* Use records where appropriate for immutable DTOs.
* Avoid unnecessary inheritance.
* Prefer composition.
* Use meaningful names.
* Keep methods focused.
* Avoid premature abstraction.
* Avoid duplicated business logic.

## Spring Boot

* Use constructor injection.
* Keep controllers thin.
* Put business logic in services/domain components.
* Use repositories for persistence.
* Use DTOs for API contracts.
* Avoid exposing JPA entities directly when inappropriate.
* Use `@Transactional` intentionally.
* Do not use transactions merely by convention.
* Use centralized exception handling.

## Validation

Use Jakarta Bean Validation for structural validation.

Business rules belong in service/domain logic.

Do not rely only on frontend validation.

## Persistence

* Use Spring Data JPA where appropriate.
* Avoid N+1 query problems.
* Do not fetch unnecessary data.
* Use appropriate relationships.
* Add indexes based on actual query needs.
* Do not expose database entities directly without considering API coupling.

## Architecture

Preferred:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Controllers must not contain business rules.

Repositories must not contain business workflows.

## Error Handling

Use meaningful application/domain exceptions.

Use `@RestControllerAdvice` for API error mapping.

Never expose stack traces or internal database errors directly.

## Configuration

* Never hardcode credentials.
* Use environment variables/configuration.
* Never commit secrets.
* Keep environment-specific configuration outside source control where appropriate.

## Testing

Every meaningful business rule should have automated tests.

State-machine rules require both valid and invalid transition tests.

## Code Quality

Prefer simple code over clever code.

Do not introduce a design pattern unless it solves an identified problem.

Do not create abstractions only for hypothetical future requirements.

