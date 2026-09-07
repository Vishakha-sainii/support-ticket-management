package com.supportticket.dto;

import java.time.Instant;
import java.util.List;

public record TicketResponse(
        Long id,
        String title,
        String description,
        String priority,
        String status,
        String assignee,
        Instant createdAt,
        Instant updatedAt,
        List<CommentResponse> comments
) {
}
