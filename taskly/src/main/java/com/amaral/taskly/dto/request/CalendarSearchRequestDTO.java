package com.amaral.taskly.dto.request;

import java.time.LocalDateTime;

public record CalendarSearchRequestDTO(
    String title,
    String status,
    LocalDateTime startDate,
    LocalDateTime endDate
) {}
