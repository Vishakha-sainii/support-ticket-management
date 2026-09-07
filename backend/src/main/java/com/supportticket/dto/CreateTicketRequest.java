package com.supportticket.dto;

import com.supportticket.entity.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTicketRequest(
        @NotBlank @Size(max = 200) String title,
        @NotBlank @Size(max = 5000) String description,
        @NotNull Priority priority,
        @NotBlank @Size(max = 100) String assignee
) {
}
