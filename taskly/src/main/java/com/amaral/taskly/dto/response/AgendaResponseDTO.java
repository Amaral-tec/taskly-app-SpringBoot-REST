package com.amaral.taskly.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.amaral.taskly.enums.RecurrenceType;
import com.amaral.taskly.enums.AgendaStatus;

public record AgendaResponseDTO(
    Long id,
    UUID publicId,
    String title,
    String description,
    LocalDateTime startDateTime,
    LocalDateTime endDateTime,
    LocalDateTime reminder,
    RecurrenceType recurrenceType,
    AgendaStatus status,
    Long userId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
