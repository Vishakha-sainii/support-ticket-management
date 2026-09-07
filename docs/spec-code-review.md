# Specification vs Code Review

**Date:** 2026-09-07  
**Scope:** Compare implementation against `spec/` artifacts.

## Summary

| Area | Result |
|------|--------|
| Functional requirements (REQ-001–REQ-010) | PASS |
| Business rules (BR-001–BR-006) | PASS |
| API contract | PASS (one non-spec endpoint noted) |
| State machine | PASS |
| Data model | PASS |
| UI flow | PASS |

**Overall:** Implementation satisfies the specification. One non-blocking deviation (health endpoint) and one partial test approach for AC-010 persistence restart.

---

## Requirement Review

| Requirement | Status | Evidence | Gap |
| ----------- | ------ | -------- | --- |
| REQ-001 Create ticket | PASS | `TicketController` POST `/api/tickets`, `TicketService.createTicket` sets `OPEN` | None |
| REQ-002 List tickets | PASS | GET `/api/tickets`, `TicketSummaryResponse` fields in API + `TicketTable` | None |
| REQ-003 View ticket | PASS | GET `/api/tickets/{id}` with comments | None |
| REQ-004 Update ticket | PASS | PUT `/api/tickets/{id}`, status rejected if present (AC-023) | None |
| REQ-005 Add comment | PASS | POST `/api/tickets/{id}/comments` | None |
| REQ-006 Search | PASS | `?search=` on list endpoint, case-insensitive repo query | None |
| REQ-007 Status filter | PASS | `?status=` on list endpoint | None |
| REQ-008 Persistence | PASS | PostgreSQL + Flyway, JPA entities | None |
| REQ-009 Backend validation | PASS | Jakarta validation, `GlobalExceptionHandler`, state machine | None |
| REQ-010 Frontend errors | PASS | `getErrorMessage`, `ErrorMessage`, form error banners | None |
| BR-001 OPEN on create | PASS | `TicketService.createTicket` | None |
| BR-002 Valid transitions only | PASS | `TicketStateMachine` | None |
| BR-003 Reject invalid transitions | PASS | `InvalidStateTransitionException` → 400 | None |
| BR-004 Ticket must exist | PASS | 404 on get/update/comment | None |
| BR-005 No blank required fields | PASS | Bean validation on DTOs/entities | None |
| BR-006 Status via state machine | PASS | PATCH `/status` only; PUT rejects `status` | None |
| API contract endpoints | PASS | All 6 documented endpoints implemented | `/api/health` is extra (dev only) |
| State machine rules | PASS | Matches `spec/state-machine.md` table | None |
| Data model fields/lengths | PASS | `Ticket`, `Comment` entities match spec | None |
| UI flow screens | PASS | List, create, detail, edit, comment, status actions | None |
| AC-010 Restart persistence | PARTIAL | `TicketApiIntegrationTest.persistedTicketRemainsAvailableAfterSubsequentRead` | No full JVM restart test (H2 in-memory) |

---

## Prioritized Gap List

1. **Low — AC-010 full restart test:** Persistence verified via repository reload and subsequent API read; not a full application restart with PostgreSQL.
2. **Low — `/api/health`:** Present for operational checks but not in API contract (acceptable for development).
3. **Resolved — Hardcoded DB password default:** Removed from `application.yml` during security review (TASK-026).

---

## Conclusion

The implementation is **spec-compliant** for all in-scope functional requirements. Remaining items are test-depth and non-contract endpoints, not missing features.
