package com.amaral.taskly.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amaral.taskly.BusinessException;
import com.amaral.taskly.dto.request.AccessRequestDTO;
import com.amaral.taskly.dto.response.AccessResponseDTO;
import com.amaral.taskly.model.Access;
import com.amaral.taskly.repository.AccessRepository;

@Service
@Transactional
public class AccessService {

    private static final Logger log = LoggerFactory.getLogger(AccessService.class);

    private final AccessRepository accessRepository;

    public AccessService(AccessRepository accessRepository) {
        this.accessRepository = accessRepository;
    }

    public AccessResponseDTO createAccess(AccessRequestDTO dto) {
        accessRepository.findByName(dto.name()).stream()
                .findFirst()
                .ifPresent(a -> {
                    throw new BusinessException("Already registered with the name: " + dto.name());
                });

        Access entity = new Access();
        entity.setName(dto.name());
        accessRepository.save(entity);

        log.info("Record successfully created, id={}", entity.getId());
        return new AccessResponseDTO(entity.getId(), entity.getName());
    }

    public AccessResponseDTO getAccess(Long id) {
        Access entity = findByIdOrThrow(id);
        log.info("Record retrieved: id={}", id);
        return new AccessResponseDTO(entity.getId(), entity.getName());
    }

    public List<AccessResponseDTO> findAllAccess() {
        List<Access> list = accessRepository.findAll();
        log.info("Retrieving all records, total={}", list.size());

        return list.stream()
                   .map(a -> new AccessResponseDTO(a.getId(), a.getName()))
                   .toList();
    }

    public List<AccessResponseDTO> findAccessByName(String name) {
        List<Access> list = accessRepository.findByName(name);
        log.info("Searching records by name={}, found={}", name, list.size());

        return list.stream()
                   .map(a -> new AccessResponseDTO(a.getId(), a.getName()))
                   .toList();
    }

    public AccessResponseDTO updateAccess(Long id, AccessRequestDTO dto) {
        Access entity = findByIdOrThrow(id);

        accessRepository.findByName(dto.name()).stream()
                .filter(a -> !a.getId().equals(id))
                .findFirst()
                .ifPresent(a -> {
                    throw new BusinessException("Already registered with the name: " + dto.name());
                });

        entity.setName(dto.name());
        accessRepository.save(entity);

        log.info("Record successfully updated, id={}", entity.getId());
        return new AccessResponseDTO(entity.getId(), entity.getName());
    }

    public void deleteAccess(Long id) {
        Access entity = findByIdOrThrow(id);
        entity.setDeleted(true);
        accessRepository.save(entity);
        log.info("Record successfully deleted, id={}", entity.getId());
    }

    private Access findByIdOrThrow(Long id) {
        return accessRepository.findById(id)
                .filter(a -> !a.getDeleted())
                .orElseThrow(() -> new BusinessException("ID not found or deleted: " + id));
    }
}
