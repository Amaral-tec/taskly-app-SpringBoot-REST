package com.amaral.taskly.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.amaral.taskly.BusinessException;
import com.amaral.taskly.dto.request.CalendarRequestDTO;
import com.amaral.taskly.dto.response.CalendarResponseDTO;
import com.amaral.taskly.enums.CalendarStatus;
import com.amaral.taskly.enums.RecurrenceType;
import com.amaral.taskly.mapper.CalendarMapper;
import com.amaral.taskly.model.Calendar;
import com.amaral.taskly.model.User;
import com.amaral.taskly.repository.CalendarRepository;

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
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final EntityManager entityManager;

    public CalendarResponseDTO createCalendar(CalendarRequestDTO dto, User user) {
        Calendar calendar = CalendarMapper.toEntity(dto, user);
        calendar.setStatus(CalendarStatus.SCHEDULED);
        calendar.setRecurrenceType(dto.recurrenceType() != null ? dto.recurrenceType() : RecurrenceType.NONE);
        calendarRepository.save(calendar);

        log.info("Calendar created, id={}", calendar.getPublicId());
        return CalendarMapper.toResponseDTO(calendar);
    }

    public CalendarResponseDTO getCalendar(UUID publicId) {
        Calendar calendar = findByIdOrThrow(publicId);
        log.info("Record retrieved: id={}", publicId);
        return CalendarMapper.toResponseDTO(calendar);
    }

    public List<CalendarResponseDTO> listCalendars(User user) {
        return calendarRepository.findByUserAndDeletedFalseOrderByStartDateTimeAsc(user).stream()
                .map(CalendarMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CalendarResponseDTO> searchCalendars(String title, String status, LocalDateTime startDate, 
                                                     LocalDateTime endDate, User user) {
        CalendarStatus statusEnum = null;
        if (status != null && !status.isBlank()) {
            try {
                statusEnum = CalendarStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status: " + status);
            }
        }        
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Calendar> cq = cb.createQuery(Calendar.class);
        Root<Calendar> root = cq.from(Calendar.class);

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

        List<Calendar> calendars = entityManager.createQuery(cq).getResultList();

        return calendars.stream()
                .map(CalendarMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CalendarResponseDTO updateCalendar(UUID publicId, CalendarRequestDTO dto, User user) {
        Calendar calendar = findByIdOrThrow(publicId);

        if (!calendar.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot edit this calendar");
        }

        calendar.setTitle(dto.title());
        calendar.setDescription(dto.description());
        calendar.setStartDateTime(dto.startDateTime());
        calendar.setEndDateTime(dto.endDateTime());
        calendar.setReminder(dto.endDateTime());
        calendar.setRecurrenceType(dto.recurrenceType() != null ? dto.recurrenceType() : RecurrenceType.NONE);
        calendar.setStatus(dto.status() != null ? dto.status() : CalendarStatus.SCHEDULED);

        calendarRepository.save(calendar);
        log.info("Calendar updated, id={}", calendar.getPublicId());
        return CalendarMapper.toResponseDTO(calendar);
    }

    public void deleteCalendar(UUID publicId, User user) {
        Calendar calendar = findByIdOrThrow(publicId);

        if (!calendar.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot delete this calendar");
        }

        calendar.setDeleted(true);
        calendarRepository.save(calendar);
        log.info("Calendar soft deleted, id={}", publicId);
    }

    public CalendarResponseDTO updateStatus(UUID publicId, CalendarStatus status, User user) {
        Calendar calendar = findByIdOrThrow(publicId);

        if (!calendar.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot mark this calendar as completed");
        }

        calendar.setStatus(status);
        calendarRepository.save(calendar);
        log.info("Calendar status updated, id={}", publicId);
        return CalendarMapper.toResponseDTO(calendar);
    }

    public CalendarResponseDTO setReminder(UUID publicId, LocalDateTime reminder, User user) {
                                           Calendar calendar = findByIdOrThrow(publicId);
        if (!calendar.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot set reminder for this calendar");
        }

        calendar.setReminder(reminder);
        calendarRepository.save(calendar);
        log.info("Reminder set for calendar, id={}", publicId);
        return CalendarMapper.toResponseDTO(calendar);
    }

    private Calendar findByIdOrThrow(UUID publicId) {
        return calendarRepository.findByPublicId(publicId)
                .filter(a -> !a.getDeleted())
                .orElseThrow(() -> new BusinessException("ID not found or deleted: " + publicId));
    }
}
