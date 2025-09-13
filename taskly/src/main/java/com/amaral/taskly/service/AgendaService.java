package com.amaral.taskly.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.amaral.taskly.BusinessException;
import com.amaral.taskly.dto.request.AgendaRequestDTO;
import com.amaral.taskly.dto.response.AgendaResponseDTO;
import com.amaral.taskly.enums.AgendaStatus;
import com.amaral.taskly.enums.RecurrenceType;
import com.amaral.taskly.mapper.AgendaMapper;
import com.amaral.taskly.model.Agenda;
import com.amaral.taskly.model.User;
import com.amaral.taskly.repository.AgendaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final EntityManager entityManager;

    public AgendaResponseDTO createAgenda(AgendaRequestDTO dto, User user) {
        Agenda agenda = AgendaMapper.toEntity(dto, user);
        agenda.setStatus(AgendaStatus.SCHEDULED);
        agenda.setRecurrenceType(dto.recurrenceType() != null ? dto.recurrenceType() : RecurrenceType.NONE);
        agendaRepository.save(agenda);

        log.info("Agenda created, id={}", agenda.getPublicId());
        return AgendaMapper.toResponseDTO(agenda);
    }

    public AgendaResponseDTO getAgenda(UUID publicId) {
        Agenda agenda = findByIdOrThrow(publicId);
        log.info("Record retrieved: id={}", publicId);
        return AgendaMapper.toResponseDTO(agenda);
    }

    public List<AgendaResponseDTO> listAgendas(User user) {
        return agendaRepository.findByUserAndDeletedFalse(user).stream()
                .map(AgendaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AgendaResponseDTO> searchAgendas(String title, String status, LocalDateTime startDate, 
                                                 LocalDateTime endDate, User user) {
        AgendaStatus statusEnum = null;
        if (status != null && !status.isBlank()) {
            try {
                statusEnum = AgendaStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status: " + status);
            }
        }        
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Agenda> cq = cb.createQuery(Agenda.class);
        Root<Agenda> root = cq.from(Agenda.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("user"), user));
        predicates.add(cb.isFalse(root.get("deleted")));

        if (title != null && !title.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }
        if (statusEnum != null) {
            predicates.add(cb.equal(root.get("status"), statusEnum));
        }
        if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("startDateTime"), startDate));
        }
        if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("endDateTime"), endDate));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(root.get("startDateTime")));

        List<Agenda> agendas = entityManager.createQuery(cq).getResultList();

        return agendas.stream()
                .map(AgendaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public AgendaResponseDTO updateAgenda(UUID publicId, AgendaRequestDTO dto, User user) {
        Agenda agenda = findByIdOrThrow(publicId);

        if (!agenda.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot edit this agenda");
        }

        agenda.setTitle(dto.title());
        agenda.setDescription(dto.description());
        agenda.setStartDateTime(dto.startDateTime());
        agenda.setEndDateTime(dto.endDateTime());
        agenda.setReminder(dto.endDateTime());
        agenda.setRecurrenceType(dto.recurrenceType() != null ? dto.recurrenceType() : RecurrenceType.NONE);
        agenda.setStatus(dto.status() != null ? dto.status() : AgendaStatus.SCHEDULED);

        agendaRepository.save(agenda);
        log.info("Agenda updated, id={}", agenda.getPublicId());
        return AgendaMapper.toResponseDTO(agenda);
    }

    public void deleteAgenda(UUID publicId, User user) {
        Agenda agenda = findByIdOrThrow(publicId);

        if (!agenda.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot delete this agenda");
        }

        agenda.setDeleted(true);
        agendaRepository.save(agenda);
        log.info("Agenda soft deleted, id={}", publicId);
    }

    public AgendaResponseDTO markAsCompleted(UUID publicId, User user) {
        Agenda agenda = findByIdOrThrow(publicId);

        if (!agenda.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot mark this agenda as completed");
        }

        agenda.setStatus(AgendaStatus.COMPLETED);
        agendaRepository.save(agenda);
        log.info("Agenda marked as completed, id={}", publicId);
        return AgendaMapper.toResponseDTO(agenda);
    }

    public AgendaResponseDTO setReminder(UUID publicId, LocalDateTime reminder, User user) {
        Agenda agenda = findByIdOrThrow(publicId);

        if (!agenda.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot set reminder for this agenda");
        }

        agenda.setReminder(reminder);
        agendaRepository.save(agenda);
        log.info("Reminder set for agenda, id={}", publicId);
        return AgendaMapper.toResponseDTO(agenda);
    }

    private Agenda findByIdOrThrow(UUID publicId) {
        return agendaRepository.findByPublicId(publicId)
                .filter(a -> !a.getDeleted())
                .orElseThrow(() -> new BusinessException("ID not found or deleted: " + publicId));
    }
}
