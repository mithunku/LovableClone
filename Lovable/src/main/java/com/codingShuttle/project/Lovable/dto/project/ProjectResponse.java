package com.codingShuttle.project.Lovable.dto.project;

import com.codingShuttle.project.Lovable.dto.auth.UserProfileResponse;

import java.time.Instant;

public record ProjectResponse(Long id,
                              String name,
                              Instant createdAt,
                              Instant updatedAt
                              ) {
}
