package com.amaral.taskly.dto.request;

import java.util.List;

public record CalendarSearchRequestDTO(
    String title,
    List<String> status,
    String startDate,
    String endDate
) {}
