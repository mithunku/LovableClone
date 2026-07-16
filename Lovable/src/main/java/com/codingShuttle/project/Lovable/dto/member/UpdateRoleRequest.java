package com.codingShuttle.project.Lovable.dto.member;

import com.codingShuttle.project.Lovable.enums.ProjectRole;

public record UpdateRoleRequest(
        ProjectRole role
) {
}
