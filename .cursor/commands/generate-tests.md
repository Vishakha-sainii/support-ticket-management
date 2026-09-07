# /generate-tests

Act as a Senior Test Engineer.

Read:

* spec/requirements.md
* spec/acceptance-criteria.md
* spec/test-strategy.md
* spec/state-machine.md
* current implementation

Identify missing meaningful automated tests.

Prioritize:

1. business rules
2. state transitions
3. invalid transitions
4. backend validation
5. API behavior
6. persistence
7. search/filter
8. comments

Do not generate tests merely to increase coverage.

Do not modify production behavior.

Add only valuable tests.

Run the relevant tests after creation.

Summarize:

* tests added
* requirements covered
* failures found
* fixes made

