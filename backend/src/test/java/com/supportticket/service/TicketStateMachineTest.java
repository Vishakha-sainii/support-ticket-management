package com.supportticket.service;

import com.supportticket.entity.Status;
import com.supportticket.exception.InvalidStateTransitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TicketStateMachineTest {

    private TicketStateMachine ticketStateMachine;

    @BeforeEach
    void setUp() {
        ticketStateMachine = new TicketStateMachine();
    }

    @Test
    void openToInProgressIsAllowed() {
        assertThatCode(() -> ticketStateMachine.validateTransition(Status.OPEN, Status.IN_PROGRESS))
                .doesNotThrowAnyException();
    }

    @Test
    void inProgressToResolvedIsAllowed() {
        assertThatCode(() -> ticketStateMachine.validateTransition(Status.IN_PROGRESS, Status.RESOLVED))
                .doesNotThrowAnyException();
    }

    @Test
    void resolvedToClosedIsAllowed() {
        assertThatCode(() -> ticketStateMachine.validateTransition(Status.RESOLVED, Status.CLOSED))
                .doesNotThrowAnyException();
    }

    @Test
    void openToCancelledIsAllowed() {
        assertThatCode(() -> ticketStateMachine.validateTransition(Status.OPEN, Status.CANCELLED))
                .doesNotThrowAnyException();
    }

    @Test
    void inProgressToCancelledIsAllowed() {
        assertThatCode(() -> ticketStateMachine.validateTransition(Status.IN_PROGRESS, Status.CANCELLED))
                .doesNotThrowAnyException();
    }

    @Test
    void closedToOpenIsRejected() {
        assertInvalidTransition(Status.CLOSED, Status.OPEN);
    }

    @Test
    void resolvedToOpenIsRejected() {
        assertInvalidTransition(Status.RESOLVED, Status.OPEN);
    }

    @Test
    void cancelledToOpenIsRejected() {
        assertInvalidTransition(Status.CANCELLED, Status.OPEN);
    }

    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"OPEN", "IN_PROGRESS", "RESOLVED", "CLOSED", "CANCELLED"})
    void sameStatusTransitionIsRejected(Status status) {
        assertInvalidTransition(status, status);
    }

    @Test
    void openToResolvedIsRejected() {
        assertInvalidTransition(Status.OPEN, Status.RESOLVED);
    }

    @Test
    void inProgressToClosedIsRejected() {
        assertInvalidTransition(Status.IN_PROGRESS, Status.CLOSED);
    }

    private void assertInvalidTransition(Status current, Status target) {
        assertThatThrownBy(() -> ticketStateMachine.validateTransition(current, target))
                .isInstanceOf(InvalidStateTransitionException.class)
                .hasMessage("Cannot transition from " + current + " to " + target);
    }
}
