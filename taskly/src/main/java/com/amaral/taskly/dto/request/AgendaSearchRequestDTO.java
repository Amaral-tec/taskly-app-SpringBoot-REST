package com.amaral.taskly.dto.request;

import java.time.LocalDateTime;

public record AgendaSearchRequestDTO(
    String title,
    String status,
    LocalDateTime startDate,
    LocalDateTime endDate
) {}
