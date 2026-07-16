package com.codingShuttle.project.Lovable.dto.project;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequest(@NotBlank(message = "Project name should not be blank") String name) {
}
