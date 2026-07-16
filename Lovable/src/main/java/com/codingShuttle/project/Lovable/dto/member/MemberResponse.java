package com.codingShuttle.project.Lovable.dto.member;

import com.codingShuttle.project.Lovable.entity.Project;
import com.codingShuttle.project.Lovable.enums.ProjectRole;

import java.time.Instant;

public record MemberResponse(
        Long userId,
        String username,
        String name,
        String avatarUrl,
        ProjectRole projectRole,
        Instant invitedAt
) {
}
