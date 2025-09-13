package com.amaral.taskly.mapper;

import com.amaral.taskly.model.Agenda;
import com.amaral.taskly.model.User;
import com.amaral.taskly.dto.request.AgendaRequestDTO;
import com.amaral.taskly.dto.response.AgendaResponseDTO;

public class AgendaMapper {

    private AgendaMapper() {
    }

    public static Agenda toEntity(AgendaRequestDTO dto, User user) {
        Agenda agenda = new Agenda();
        agenda.setTitle(dto.title());
        agenda.setDescription(dto.description());
        agenda.setStartDateTime(dto.startDateTime());
        agenda.setEndDateTime(dto.endDateTime());
        agenda.setUser(user);
        return agenda;
    }

    public static AgendaResponseDTO toResponseDTO(Agenda agenda) {
        return new AgendaResponseDTO(
                agenda.getId(),
                agenda.getPublicId(),
                agenda.getTitle(),
                agenda.getDescription(),
                agenda.getStartDateTime(),
                agenda.getEndDateTime(),
                agenda.getReminder(),
                agenda.getRecurrenceType(),
                agenda.getStatus(),
                agenda.getUser() != null ? agenda.getUser().getId() : null,
                agenda.getCreatedAt(),
                agenda.getUpdatedAt()
        );
    }
}
