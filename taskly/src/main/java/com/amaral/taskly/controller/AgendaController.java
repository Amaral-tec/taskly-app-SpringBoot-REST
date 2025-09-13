package com.amaral.taskly.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amaral.taskly.dto.request.AgendaRequestDTO;
import com.amaral.taskly.dto.request.AgendaSearchRequestDTO;
import com.amaral.taskly.dto.response.AgendaResponseDTO;
import com.amaral.taskly.model.User;
import com.amaral.taskly.service.AgendaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/agendas")
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaService agendaService;  

    @PostMapping
    public ResponseEntity<AgendaResponseDTO> createAgenda(
            @RequestBody @Valid AgendaRequestDTO dto,
            @AuthenticationPrincipal User currentUser) {

        AgendaResponseDTO response = agendaService.createAgenda(dto, currentUser);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<AgendaResponseDTO> getAgenda(@PathVariable UUID publicId) {
        return ResponseEntity.ok(agendaService.getAgenda(publicId));
    }

    @GetMapping
    public ResponseEntity<List<AgendaResponseDTO>> listAgendas(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(agendaService.listAgendas(currentUser));
    }

    @PostMapping("/search")
    public ResponseEntity<List<AgendaResponseDTO>> searchAgendas(
            @RequestBody AgendaSearchRequestDTO filters,
            @AuthenticationPrincipal User currentUser) {

        List<AgendaResponseDTO> agendas = agendaService.searchAgendas(
            filters.title(),
            filters.status(),
            filters.startDate(),
            filters.endDate(),
            currentUser
        );
        return ResponseEntity.ok(agendas);
    }

    @PutMapping("/{publicId}")
    public ResponseEntity<AgendaResponseDTO> updateAgenda(
            @PathVariable UUID publicId,
            @RequestBody @Valid AgendaRequestDTO dto,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(agendaService.updateAgenda(publicId, dto, currentUser));
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<Void> deleteAgenda(
            @PathVariable UUID publicId,
            @AuthenticationPrincipal User currentUser) {

        agendaService.deleteAgenda(publicId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{publicId}/complete")
    public ResponseEntity<AgendaResponseDTO> completeAgenda(
            @PathVariable UUID publicId,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(agendaService.markAsCompleted(publicId, currentUser));
    }

    @PostMapping("/{publicId}/reminder")
    public ResponseEntity<AgendaResponseDTO> setReminder(
            @PathVariable UUID publicId,
            @RequestParam LocalDateTime reminder,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(agendaService.setReminder(publicId, reminder, currentUser));
    }
}
