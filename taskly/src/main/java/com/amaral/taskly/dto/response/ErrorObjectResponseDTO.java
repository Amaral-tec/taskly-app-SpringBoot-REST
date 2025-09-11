package com.amaral.taskly.dto.response;

import java.time.LocalDateTime;

public record ErrorObjectResponseDTO(
    String error,
    String code,
    String path,
    LocalDateTime timestamp
) {}
