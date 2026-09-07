package com.supportticket.exception;

public class StatusFieldNotAllowedException extends RuntimeException {

    public StatusFieldNotAllowedException() {
        super("Status cannot be changed through ticket update; use PATCH /api/tickets/{id}/status");
    }
}
