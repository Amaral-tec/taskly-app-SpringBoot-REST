package com.amaral.taskly.dto.request;

public record CalendarSearchRequestDTO(
    String title,
    String status,
    String startDate,
    String endDate
) {}
