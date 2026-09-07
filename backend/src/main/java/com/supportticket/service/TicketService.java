package com.supportticket.service;

import com.supportticket.dto.CommentResponse;
import com.supportticket.dto.CreateCommentRequest;
import com.supportticket.dto.CreateTicketRequest;
import com.supportticket.dto.TicketResponse;
import com.supportticket.dto.TicketSummaryResponse;
import com.supportticket.dto.UpdateStatusRequest;
import com.supportticket.dto.UpdateTicketRequest;
import com.supportticket.entity.Comment;
import com.supportticket.entity.Status;
import com.supportticket.entity.Ticket;
import com.supportticket.exception.StatusFieldNotAllowedException;
import com.supportticket.exception.TicketNotFoundException;
import com.supportticket.mapper.TicketMapper;
import com.supportticket.repository.CommentRepository;
import com.supportticket.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final CommentRepository commentRepository;
    private final TicketStateMachine ticketStateMachine;

    public TicketService(
            TicketRepository ticketRepository,
            CommentRepository commentRepository,
            TicketStateMachine ticketStateMachine
    ) {
        this.ticketRepository = ticketRepository;
        this.commentRepository = commentRepository;
        this.ticketStateMachine = ticketStateMachine;
    }

    @Transactional
    public TicketResponse createTicket(CreateTicketRequest request) {
        Ticket ticket = new Ticket();
        ticket.setTitle(request.title());
        ticket.setDescription(request.description());
        ticket.setPriority(request.priority());
        ticket.setStatus(Status.OPEN);
        ticket.setAssignee(request.assignee());

        Ticket saved = ticketRepository.save(ticket);
        return TicketMapper.toDetail(saved);
    }

    @Transactional(readOnly = true)
    public List<TicketSummaryResponse> listTickets(Status status, String search) {
        String keyword = normalizeSearch(search);
        return ticketRepository.findByFilters(status, keyword).stream()
                .map(TicketMapper::toSummary)
                .toList();
    }

    @Transactional(readOnly = true)
    public TicketResponse getTicket(Long id) {
        Ticket ticket = ticketRepository.findByIdWithComments(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
        return TicketMapper.toDetail(ticket);
    }

    @Transactional
    public TicketResponse updateTicket(Long id, UpdateTicketRequest request) {
        if (request.status() != null) {
            throw new StatusFieldNotAllowedException();
        }

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));

        ticket.setTitle(request.title());
        ticket.setDescription(request.description());
        ticket.setPriority(request.priority());
        ticket.setAssignee(request.assignee());

        Ticket saved = ticketRepository.save(ticket);
        return TicketMapper.toDetail(saved);
    }

    @Transactional
    public CommentResponse addComment(Long ticketId, CreateCommentRequest request) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        Comment comment = new Comment();
        comment.setText(request.text());
        comment.setTicket(ticket);

        Comment saved = commentRepository.saveAndFlush(comment);
        return TicketMapper.toComment(saved);
    }

    @Transactional
    public TicketResponse transitionStatus(Long id, UpdateStatusRequest request) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));

        ticketStateMachine.validateTransition(ticket.getStatus(), request.status());
        ticket.setStatus(request.status());

        Ticket saved = ticketRepository.save(ticket);
        return TicketMapper.toDetail(saved);
    }

    private String normalizeSearch(String search) {
        if (search == null || search.isBlank()) {
            return null;
        }
        return search;
    }
}
