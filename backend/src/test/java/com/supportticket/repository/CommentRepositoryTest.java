package com.supportticket.repository;

import com.supportticket.entity.Comment;
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
class CommentRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void savesCommentAssociatedWithTicket() {
        Ticket ticket = ticketRepository.save(createTicket());

        Comment comment = new Comment();
        comment.setText("Investigating the issue.");
        ticket.addComment(comment);

        ticketRepository.saveAndFlush(ticket);

        List<Comment> persistedComments = commentRepository.findByTicket_IdOrderByCreatedAtAsc(ticket.getId());

        assertThat(persistedComments).hasSize(1);
        assertThat(persistedComments.getFirst().getId()).isNotNull();
        assertThat(persistedComments.getFirst().getTicketId()).isEqualTo(ticket.getId());
        assertThat(persistedComments.getFirst().getCreatedAt()).isNotNull();
    }

    @Test
    void findByTicketIdReturnsCommentsInCreationOrder() {
        Ticket ticket = ticketRepository.save(createTicket());

        Comment first = new Comment();
        first.setText("First comment");
        ticket.addComment(first);

        Comment second = new Comment();
        second.setText("Second comment");
        ticket.addComment(second);

        ticketRepository.save(ticket);

        List<Comment> comments = commentRepository.findByTicket_IdOrderByCreatedAtAsc(ticket.getId());

        assertThat(comments).hasSize(2);
        assertThat(comments.get(0).getText()).isEqualTo("First comment");
        assertThat(comments.get(1).getText()).isEqualTo("Second comment");
    }

    private Ticket createTicket() {
        Ticket ticket = new Ticket();
        ticket.setTitle("Unable to login");
        ticket.setDescription("User cannot login to the application");
        ticket.setPriority(Priority.HIGH);
        ticket.setStatus(Status.OPEN);
        ticket.setAssignee("support-user");
        return ticket;
    }
}
