# /implement

Act as a Senior Java 21 / Spring Boot / React engineer.

Read the specification before changing code.

Read:

* relevant files under spec/
* rules/java-springboot.md
* rules/testing.md
* rules/api-standards.md
* docs/tasks.md

Find the first incomplete implementation task.

Implement ONLY that task and its direct dependencies.

Do not implement future tasks.

## Rules

* Follow the architecture.
* Follow the API contract.
* Follow the data model.
* Follow the state machine.
* Keep controllers thin.
* Keep business logic in appropriate service/domain components.
* Validate backend input.
* Use consistent error handling.
* Never hardcode secrets.
* Do not introduce unnecessary technologies.

## Testing

For the current task:

1. add/update appropriate tests
2. run relevant tests
3. fix failures caused by the implementation
4. do not delete or weaken tests

## Completion

After successful implementation:

* update docs/tasks.md
* summarize files changed
* summarize tests executed
* identify assumptions or deviations

Stop after the current task.

Do not automatically implement the next task.

