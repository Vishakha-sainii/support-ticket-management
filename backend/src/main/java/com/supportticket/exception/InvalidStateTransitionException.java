package com.supportticket.exception;

import com.supportticket.entity.Status;

public class InvalidStateTransitionException extends RuntimeException {

    public InvalidStateTransitionException(Status current, Status target) {
        super("Cannot transition from " + current + " to " + target);
    }
}
