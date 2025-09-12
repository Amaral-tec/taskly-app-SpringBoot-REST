package com.amaral.taskly.dto.response;

import java.util.UUID;

public record AccessResponseDTO(
    UUID publicId,
    String name
) {}
