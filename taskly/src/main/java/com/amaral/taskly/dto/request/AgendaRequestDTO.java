package com.amaral.taskly.dto.request;

import java.time.LocalDateTime;

import com.amaral.taskly.enums.AgendaStatus;
import com.amaral.taskly.enums.RecurrenceType;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AgendaRequestDTO(

    @NotBlank(message = "Title is required")
    String title,

    String description,

    @NotNull(message = "Start date and time is required")
    @FutureOrPresent(message = "Start date and time must be in the present or future")
    LocalDateTime startDateTime,

    @NotNull(message = "End date and time is required")
    @FutureOrPresent(message = "End date and time must be in the present or future")
    LocalDateTime endDateTime,

    LocalDateTime reminder,

    @NotNull(message = "Recurrence type is required")
    RecurrenceType recurrenceType,

    AgendaStatus status,

    Long userId
) {}
