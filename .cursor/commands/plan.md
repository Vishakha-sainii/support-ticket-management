# /plan

Act as a Senior Software Architect and Requirements Engineer.

Read the assignment context and all existing project artifacts.

First inspect:

* spec/
* rules/
* skills/
* docs/

Do NOT implement application code.

Do NOT create controllers, services, repositories, entities, frontend components, migrations or tests.

Your job is to produce a complete implementation-ready plan.

## Required checks

Review:

* requirements.md
* acceptance-criteria.md
* architecture.md
* data-model.md
* api-contract.md
* state-machine.md
* ui-flow.md
* test-strategy.md
* docs/tasks.md

Ensure consistency between all artifacts.

## Identify

* missing requirements
* contradictory requirements
* missing acceptance criteria
* API inconsistencies
* data-model gaps
* state-machine gaps
* UI/API mismatches
* testing gaps
* implementation task gaps
* unnecessary complexity

## Output

Update only specification/planning documentation where necessary.

Do not implement code.

Ensure every requirement maps to:

Requirement
→ Acceptance Criteria
→ Task
→ Test Strategy

Ensure the backend remains authoritative for business rules and state transitions.

At the end provide:

1. specification changes
2. implementation order
3. dependencies between tasks
4. risks
5. assumptions
6. unresolved questions

Do not begin implementation.

