package com.supportticket.controller;

import com.supportticket.dto.CommentResponse;
import com.supportticket.dto.CreateCommentRequest;
import com.supportticket.dto.CreateTicketRequest;
import com.supportticket.dto.TicketResponse;
import com.supportticket.dto.TicketSummaryResponse;
import com.supportticket.dto.UpdateStatusRequest;
import com.supportticket.dto.UpdateTicketRequest;
import com.supportticket.entity.Status;
import com.supportticket.exception.InvalidQueryParameterException;
import com.supportticket.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody CreateTicketRequest request) {
        TicketResponse response = ticketService.createTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<TicketSummaryResponse> listTickets(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search
    ) {
        return ticketService.listTickets(parseStatus(status), search);
    }

    @GetMapping("/{id}")
    public TicketResponse getTicket(@PathVariable Long id) {
        return ticketService.getTicket(id);
    }

    @PutMapping("/{id}")
    public TicketResponse updateTicket(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTicketRequest request
    ) {
        return ticketService.updateTicket(id, request);
    }

    @PatchMapping("/{id}/status")
    public TicketResponse transitionStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request
    ) {
        return ticketService.transitionStatus(id, request);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long id,
            @Valid @RequestBody CreateCommentRequest request
    ) {
        CommentResponse response = ticketService.addComment(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private Status parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        try {
            return Status.valueOf(status);
        } catch (IllegalArgumentException ex) {
            throw new InvalidQueryParameterException("Invalid status filter: " + status);
        }
    }
}
