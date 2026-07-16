package com.codingShuttle.project.Lovable.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProjectPermission {
    VIEW,
    EDIT,
    DELETE,
    MANAGE_MEMBERS,
    VIEW_MEMBERS;

}
