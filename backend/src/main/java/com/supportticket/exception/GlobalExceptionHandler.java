package com.supportticket.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(TicketNotFoundException ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage(), request);
    }

    @ExceptionHandler(InvalidStateTransitionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidStateTransition(
            InvalidStateTransitionException ex,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.BAD_REQUEST, "INVALID_STATE_TRANSITION", ex.getMessage(), request);
    }

    @ExceptionHandler({
            StatusFieldNotAllowedException.class,
            InvalidQueryParameterException.class
    })
    public ResponseEntity<ErrorResponse> handleBusinessValidation(RuntimeException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Request validation failed");
        return buildError(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Malformed request payload", request);
    }

    private ResponseEntity<ErrorResponse> buildError(
            HttpStatus status,
            String error,
            String message,
            HttpServletRequest request
    ) {
        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                status.value(),
                error,
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(body);
    }
}
