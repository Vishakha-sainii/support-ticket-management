# /implement

Act as a Senior Java 21 / Spring Boot / React engineer.

Your goal is to implement the **next logical feature group** from `docs/tasks.md`, not just one task at a time.

## Before Implementation

Read:

* relevant files under `spec/`
* `rules/java-springboot.md`
* `rules/testing.md`
* `rules/api-standards.md`
* `docs/tasks.md`
* existing implementation/code related to the incomplete tasks

First inspect the current code and `docs/tasks.md`.

Determine which tasks are already genuinely completed. Do not reimplement completed work.

Then identify the **next cohesive group of incomplete tasks** that can safely be implemented together based on dependencies.

## Implementation Scope

Implement the next logical group of related tasks.

Prefer grouping tasks that form one complete feature, for example:

* project/database foundation
* domain/entities/repositories
* ticket CRUD
* comments/search/filter
* state machine/status transitions/error handling
* frontend feature groups
* testing/documentation/review tasks

Do NOT implement the entire remaining project in one session.

Do NOT implement unrelated future features.

Do NOT stop after every individual task if the tasks belong to the same cohesive feature group.

Continue until the current logical feature group is complete and tested.

## Rules

* Follow the architecture.
* Follow the API contract.
* Follow the data model.
* Follow the state machine.
* Keep controllers thin.
* Keep business logic in service/domain components.
* Validate backend input.
* Use consistent error handling.
* Never hardcode secrets.
* Do not introduce unnecessary technologies.
* Do not modify requirements to make implementation easier.
* Do not weaken or remove existing tests.
* Do not implement unrelated tasks.
* Do not duplicate existing functionality.

## Testing

For all tasks implemented in this session:

1. Add/update appropriate tests.
2. Run relevant tests.
3. Fix failures caused by the implementation.
4. Do not delete or weaken tests.
5. Ensure existing functionality continues to work.

## Task Tracking

After successful implementation:

* Update `docs/tasks.md`.
* Mark **only genuinely completed tasks** as completed.
* Keep incomplete tasks unchanged.
* Do not mark a task complete merely because some code was created; verify its acceptance criteria and tests.

## Completion

At the end, report:

* Logical feature group implemented.
* TASK IDs completed.
* TASK IDs remaining.
* Files changed.
* Tests added/updated.
* Tests executed and results.
* Assumptions or deviations.
* Any blockers for the next implementation group.

Then STOP.

Do not automatically implement another logical feature group.

