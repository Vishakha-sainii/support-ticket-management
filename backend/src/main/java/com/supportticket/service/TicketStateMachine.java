package com.supportticket.service;

import com.supportticket.entity.Status;
import com.supportticket.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class TicketStateMachine {

    private static final Map<Status, Set<Status>> ALLOWED_TRANSITIONS = Map.of(
            Status.OPEN, Set.of(Status.IN_PROGRESS, Status.CANCELLED),
            Status.IN_PROGRESS, Set.of(Status.RESOLVED, Status.CANCELLED),
            Status.RESOLVED, Set.of(Status.CLOSED),
            Status.CLOSED, Set.of(),
            Status.CANCELLED, Set.of()
    );

    public void validateTransition(Status current, Status target) {
        if (current == target) {
            throw new InvalidStateTransitionException(current, target);
        }

        Set<Status> allowedTargets = ALLOWED_TRANSITIONS.getOrDefault(current, Set.of());
        if (!allowedTargets.contains(target)) {
            throw new InvalidStateTransitionException(current, target);
        }
    }
}
