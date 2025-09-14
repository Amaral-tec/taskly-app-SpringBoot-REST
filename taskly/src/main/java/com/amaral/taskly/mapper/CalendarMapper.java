package com.amaral.taskly.mapper;

import com.amaral.taskly.model.Calendar;
import com.amaral.taskly.model.User;
import com.amaral.taskly.dto.request.CalendarRequestDTO;
import com.amaral.taskly.dto.response.CalendarResponseDTO;

public class CalendarMapper {

    private CalendarMapper() {
    }

    public static Calendar toEntity(CalendarRequestDTO dto, User user) {
        Calendar calendar = new Calendar();
        calendar.setTitle(dto.title());
        calendar.setDescription(dto.description());
        calendar.setStartDateTime(dto.startDateTime());
        calendar.setEndDateTime(dto.endDateTime());
        calendar.setUser(user);
        return calendar;
    }

    public static CalendarResponseDTO toResponseDTO(Calendar calendar) {
        return new CalendarResponseDTO(
                calendar.getId(),
                calendar.getPublicId(),
                calendar.getTitle(),
                calendar.getDescription(),
                calendar.getStartDateTime(),
                calendar.getEndDateTime(),
                calendar.getReminder(),
                calendar.getRecurrenceType(),
                calendar.getStatus(),
                calendar.getUser() != null ? calendar.getUser().getId() : null,
                calendar.getCreatedAt(),
                calendar.getUpdatedAt()
        );
    }
}
