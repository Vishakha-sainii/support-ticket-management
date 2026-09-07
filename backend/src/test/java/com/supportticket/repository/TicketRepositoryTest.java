package com.supportticket.repository;

import com.supportticket.entity.Priority;
import com.supportticket.entity.Status;
import com.supportticket.entity.Ticket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    void savesAndRetrievesTicket() {
        Ticket ticket = createTicket("Login issue", "Cannot login", Status.OPEN);

        Ticket saved = ticketRepository.save(ticket);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
        assertThat(ticketRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void findByStatusReturnsOnlyMatchingTickets() {
        ticketRepository.save(createTicket("Open ticket", "Description A", Status.OPEN));
        ticketRepository.save(createTicket("Closed ticket", "Description B", Status.CLOSED));

        List<Ticket> openTickets = ticketRepository.findByStatus(Status.OPEN);

        assertThat(openTickets).hasSize(1);
        assertThat(openTickets.getFirst().getTitle()).isEqualTo("Open ticket");
    }

    @Test
    void findByFiltersAppliesStatusAndCaseInsensitiveSearch() {
        ticketRepository.save(createTicket("Login issue", "User cannot login", Status.OPEN));
        ticketRepository.save(createTicket("Billing issue", "Payment failed", Status.OPEN));
        ticketRepository.save(createTicket("Other login", "Different problem", Status.CLOSED));

        List<Ticket> results = ticketRepository.findByFilters(Status.OPEN, "LOGIN");

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getTitle()).isEqualTo("Login issue");
    }

    @Test
    void findByFiltersWithBlankKeywordReturnsAllMatchingStatus() {
        ticketRepository.save(createTicket("Ticket A", "Description A", Status.OPEN));
        ticketRepository.save(createTicket("Ticket B", "Description B", Status.RESOLVED));

        List<Ticket> results = ticketRepository.findByFilters(Status.OPEN, "");

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getTitle()).isEqualTo("Ticket A");
    }

    private Ticket createTicket(String title, String description, Status status) {
        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setPriority(Priority.HIGH);
        ticket.setStatus(status);
        ticket.setAssignee("support-user");
        return ticket;
    }
}
