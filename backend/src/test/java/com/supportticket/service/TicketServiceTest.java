package com.supportticket.service;

import com.supportticket.dto.CreateCommentRequest;
import com.supportticket.dto.CreateTicketRequest;
import com.supportticket.dto.UpdateStatusRequest;
import com.supportticket.dto.UpdateTicketRequest;
import com.supportticket.entity.Priority;
import com.supportticket.entity.Status;
import com.supportticket.entity.Ticket;
import com.supportticket.exception.InvalidStateTransitionException;
import com.supportticket.exception.StatusFieldNotAllowedException;
import com.supportticket.exception.TicketNotFoundException;
import com.supportticket.repository.CommentRepository;
import com.supportticket.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private CommentRepository commentRepository;

    private final TicketStateMachine ticketStateMachine = new TicketStateMachine();

    @InjectMocks
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        ticketService = new TicketService(ticketRepository, commentRepository, ticketStateMachine);
    }

    @Test
    void createTicketSetsOpenStatus() {
        CreateTicketRequest request = new CreateTicketRequest(
                "Title", "Description", Priority.HIGH, "assignee"
        );
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> {
            Ticket ticket = invocation.getArgument(0);
            ticket.setId(1L);
            return ticket;
        });

        var response = ticketService.createTicket(request);

        assertThat(response.status()).isEqualTo("OPEN");
        ArgumentCaptor<Ticket> captor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(Status.OPEN);
    }

    @Test
    void getTicketThrowsWhenMissing() {
        when(ticketRepository.findByIdWithComments(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.getTicket(99L))
                .isInstanceOf(TicketNotFoundException.class);
    }

    @Test
    void updateTicketRejectsStatusField() {
        UpdateTicketRequest request = new UpdateTicketRequest(
                "Title", "Description", Priority.HIGH, "assignee", Status.CLOSED
        );

        assertThatThrownBy(() -> ticketService.updateTicket(1L, request))
                .isInstanceOf(StatusFieldNotAllowedException.class);

        verify(ticketRepository, never()).save(any());
    }

    @Test
    void updateTicketThrowsWhenTicketMissing() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());
        UpdateTicketRequest request = new UpdateTicketRequest(
                "Title", "Description", Priority.HIGH, "assignee", null
        );

        assertThatThrownBy(() -> ticketService.updateTicket(99L, request))
                .isInstanceOf(TicketNotFoundException.class);
    }

    @Test
    void addCommentThrowsWhenTicketMissing() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.addComment(99L, new CreateCommentRequest("text")))
                .isInstanceOf(TicketNotFoundException.class);
    }

    @Test
    void listTicketsNormalizesBlankSearch() {
        when(ticketRepository.findByFilters(eq(Status.OPEN), eq(null))).thenReturn(List.of());

        ticketService.listTickets(Status.OPEN, "   ");

        verify(ticketRepository).findByFilters(Status.OPEN, null);
    }

    @Test
    void transitionStatusAppliesValidTransition() {
        Ticket ticket = sampleTicket(Status.OPEN);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        var response = ticketService.transitionStatus(1L, new UpdateStatusRequest(Status.IN_PROGRESS));

        assertThat(response.status()).isEqualTo("IN_PROGRESS");
        assertThat(ticket.getStatus()).isEqualTo(Status.IN_PROGRESS);
    }

    @Test
    void transitionStatusRejectsInvalidTransition() {
        Ticket ticket = sampleTicket(Status.CLOSED);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        assertThatThrownBy(() -> ticketService.transitionStatus(
                1L, new UpdateStatusRequest(Status.OPEN)))
                .isInstanceOf(InvalidStateTransitionException.class);

        verify(ticketRepository, never()).save(any());
        assertThat(ticket.getStatus()).isEqualTo(Status.CLOSED);
    }

    private Ticket sampleTicket(Status status) {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("Title");
        ticket.setDescription("Description");
        ticket.setPriority(Priority.MEDIUM);
        ticket.setStatus(status);
        ticket.setAssignee("assignee");
        return ticket;
    }
}
