# Requirements Traceability Matrix

## Functional Requirements

| Requirement | Acceptance Criteria | Task | Implementation | Tests | Status |
| ----------- | ------------------- | ---- | -------------- | ----- | ------ |
| REQ-001 | AC-001, AC-002 | TASK-007, TASK-017 | `TicketController.createTicket`, `TicketService.createTicket`, `CreateTicketPage` | `TicketControllerTest`, `TicketServiceTest`, `TicketApiIntegrationTest` | Complete |
| REQ-002 | AC-003 | TASK-008, TASK-016 | `TicketController.listTickets`, `TicketListPage`, `TicketTable` | `TicketControllerTest`, `TicketTable.test.tsx` | Complete |
| REQ-003 | AC-004, AC-005 | TASK-008, TASK-018 | `TicketController.getTicket`, `TicketDetailPage` | `TicketControllerTest`, `TicketApiIntegrationTest` | Complete |
| REQ-004 | AC-006, AC-021, AC-023 | TASK-009, TASK-018 | `TicketController.updateTicket`, `TicketDetailPage` edit form | `TicketControllerTest`, `TicketServiceTest` | Complete |
| REQ-005 | AC-007, AC-022 | TASK-010, TASK-019 | `TicketController.addComment`, `CommentSection` | `TicketControllerTest`, `CommentRepositoryTest` | Complete |
| REQ-006 | AC-008 | TASK-011, TASK-016 | `TicketService.listTickets`, search param | `TicketControllerTest`, `TicketRepositoryTest` | Complete |
| REQ-007 | AC-009 | TASK-011, TASK-016 | `TicketService.listTickets`, status param | `TicketControllerTest`, `TicketRepositoryTest` | Complete |
| REQ-008 | AC-010 | TASK-002, TASK-021 | Flyway migration, JPA entities, PostgreSQL config | `TicketRepositoryTest`, `TicketApiIntegrationTest` | Complete |
| REQ-009 | AC-002, AC-011–AC-018, AC-023 | TASK-012–014 | `TicketStateMachine`, `GlobalExceptionHandler`, validation DTOs | `TicketStateMachineTest`, `TicketControllerTest`, `TicketServiceTest` | Complete |
| REQ-010 | AC-019 | TASK-015–019 | `utils/api.ts`, `ErrorMessage`, form error handling | `api.test.ts`, `ErrorMessage.test.tsx`, `TicketForm.test.tsx` | Complete |

## Non-Functional Requirements

| Requirement | Acceptance Criteria | Task | Implementation | Tests | Status |
| ----------- | ------------------- | ---- | -------------- | ----- | ------ |
| NFR-001 | — | TASK-001, TASK-025 | Layered Spring Boot structure | `SupportTicketApplicationTests` | Complete |
| NFR-002 | — | TASK-001, TASK-025 | Controller/Service/Repository separation | Code review (`docs/code-quality-review.md`) | Complete |
| NFR-003 | — | TASK-020–022 | Unit + integration test suites | 57 backend + 13 frontend tests | Complete |
| NFR-004 | AC-002, AC-019, AC-023 | TASK-014, TASK-021 | `GlobalExceptionHandler`, `ErrorResponse` | `TicketControllerTest`, `api.test.ts` | Complete |
| NFR-005 | AC-020 | TASK-002, TASK-026 | Env-based DB config, `.env.example` | `docs/security-review.md` | Complete |
| NFR-006 | — | TASK-001, TASK-025 | No extra infrastructure | Architecture review | Complete |

## Business Rules

| Rule | Acceptance Criteria | Task | Implementation | Tests | Status |
| ---- | ------------------- | ---- | -------------- | ----- | ------ |
| BR-001 | AC-001 | TASK-007 | `TicketService.createTicket` → `Status.OPEN` | `TicketControllerTest.createTicketReturnsOpenStatus` | Complete |
| BR-002 | AC-011–AC-015 | TASK-012, TASK-013 | `TicketStateMachine.validateTransition` | `TicketStateMachineTest`, `TicketControllerTest` | Complete |
| BR-003 | AC-016–AC-018 | TASK-012, TASK-013, TASK-022 | `InvalidStateTransitionException` | `TicketStateMachineTest`, `TicketControllerTest` | Complete |
| BR-004 | AC-005, AC-021, AC-022 | TASK-008–010 | `TicketNotFoundException` | `TicketControllerTest`, `TicketServiceTest` | Complete |
| BR-005 | AC-002 | TASK-014 | Jakarta `@NotBlank` on DTOs | `TicketControllerTest`, `TicketApiIntegrationTest` | Complete |
| BR-006 | AC-023, AC-011–AC-018 | TASK-009, TASK-012, TASK-013 | PUT rejects status; PATCH for transitions | `TicketControllerTest.updateTicketRejectsStatusField` | Complete |

Last updated: 2026-09-07 (TASK-027)
