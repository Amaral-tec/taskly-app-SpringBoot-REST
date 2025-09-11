package com.amaral.taskly.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amaral.taskly.dto.request.AccessRequestDTO;
import com.amaral.taskly.dto.response.AccessResponseDTO;
import com.amaral.taskly.service.AccessService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/access")
public class AccessController {

    private final AccessService accessService;

    public AccessController(AccessService accessService) {
        this.accessService = accessService;
    }

    @PostMapping
    public ResponseEntity<AccessResponseDTO> createAccess(@RequestBody @Valid AccessRequestDTO dto) {
        AccessResponseDTO response = accessService.createAccess(dto);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccessResponseDTO> getAccess(@PathVariable Long id) {
        AccessResponseDTO response = accessService.getAccess(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AccessResponseDTO>> findAllAccess() {
        return ResponseEntity.ok(accessService.findAllAccess());
    }

    @GetMapping("/search")
    public ResponseEntity<List<AccessResponseDTO>> findAccessByName(@RequestParam String name) {
        return ResponseEntity.ok(accessService.findAccessByName(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccessResponseDTO> updateAccess(@PathVariable Long id,
                                                          @RequestBody @Valid AccessRequestDTO dto) {
        return ResponseEntity.ok(accessService.updateAccess(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccessById(@PathVariable Long id) {
        accessService.deleteAccess(id);
        return ResponseEntity.noContent().build();
    }
}
