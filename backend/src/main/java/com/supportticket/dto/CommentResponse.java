package com.supportticket.dto;

import java.time.Instant;

public record CommentResponse(
        Long id,
        Long ticketId,
        String text,
        Instant createdAt
) {
}
