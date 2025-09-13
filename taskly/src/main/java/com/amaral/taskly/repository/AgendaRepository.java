package com.amaral.taskly.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amaral.taskly.model.Agenda;
import com.amaral.taskly.model.User;

import jakarta.transaction.Transactional;

@Transactional
public interface AgendaRepository extends JpaRepository<Agenda, Long> {

    List<Agenda> findByUserAndDeletedFalse(User user);

    Optional<Agenda> findByPublicId(UUID publicId);
}
