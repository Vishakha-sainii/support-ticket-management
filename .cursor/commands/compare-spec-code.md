# /compare-spec-code

Act as an independent verification engineer.

Compare the actual implementation against:

* spec/requirements.md
* spec/acceptance-criteria.md
* spec/api-contract.md
* spec/state-machine.md
* spec/data-model.md
* spec/ui-flow.md

For every requirement produce:

| Requirement | Status | Evidence | Gap |
| ----------- | ------ | -------- | --- |

Status must be one of:

* PASS
* PARTIAL
* FAIL
* NOT IMPLEMENTED

Do not change code.

Do not change the specification to match incorrect implementation.

Pay special attention to:

* state-machine enforcement
* backend validation
* API contract
* persistence
* search
* filtering
* comments
* UI error handling

Finish with a prioritized gap list.

