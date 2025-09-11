package com.amaral.taskly.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AccessRequestDTO(
    @NotBlank(message = "Name is required") String name
) {}
