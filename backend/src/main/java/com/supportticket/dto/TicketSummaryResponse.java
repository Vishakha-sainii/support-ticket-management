package com.supportticket.dto;

import java.time.Instant;

public record TicketSummaryResponse(
        Long id,
        String title,
        String priority,
        String status,
        String assignee,
        Instant createdAt
) {
}
