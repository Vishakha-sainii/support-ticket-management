package com.supportticket.dto;

import com.supportticket.entity.Status;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(
        @NotNull Status status
) {
}
