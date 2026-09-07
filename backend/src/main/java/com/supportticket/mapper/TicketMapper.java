package com.supportticket.mapper;

import com.supportticket.dto.CommentResponse;
import com.supportticket.dto.TicketResponse;
import com.supportticket.dto.TicketSummaryResponse;
import com.supportticket.entity.Comment;
import com.supportticket.entity.Ticket;

import java.util.Comparator;
import java.util.List;

public final class TicketMapper {

    private TicketMapper() {
    }

    public static TicketSummaryResponse toSummary(Ticket ticket) {
        return new TicketSummaryResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getPriority().name(),
                ticket.getStatus().name(),
                ticket.getAssignee(),
                ticket.getCreatedAt()
        );
    }

    public static TicketResponse toDetail(Ticket ticket) {
        List<CommentResponse> comments = ticket.getComments().stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt))
                .map(TicketMapper::toComment)
                .toList();

        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getPriority().name(),
                ticket.getStatus().name(),
                ticket.getAssignee(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                comments
        );
    }

    public static CommentResponse toComment(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getTicketId(),
                comment.getText(),
                comment.getCreatedAt()
        );
    }
}
