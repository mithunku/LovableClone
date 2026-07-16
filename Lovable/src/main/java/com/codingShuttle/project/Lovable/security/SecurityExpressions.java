package com.codingShuttle.project.Lovable.security;

import com.codingShuttle.project.Lovable.Repository.ProjectMemberRepository;
import com.codingShuttle.project.Lovable.enums.ProjectPermission;
import com.codingShuttle.project.Lovable.enums.ProjectRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("security")
@RequiredArgsConstructor
public class SecurityExpressions {

    private final AuthUtil authUtil;
    private final ProjectMemberRepository projectMemberRepo;
    public boolean canViewProject(Long projectId)
    {
        Long userId=authUtil.getCurrentUserId();

       return  projectMemberRepo.findRoleByProjectIdAndUserId(projectId,userId)
               .map(role -> role.getProjectPermissionSet().contains(ProjectPermission.VIEW)).orElse(false);
    }

    public boolean canEditProject(Long projectId)
    {
        Long userId=authUtil.getCurrentUserId();

        return  projectMemberRepo.findRoleByProjectIdAndUserId(projectId,userId)
                .map(role -> role.getProjectPermissionSet().contains(ProjectPermission.EDIT)).orElse(false);
    }

    public boolean canDeleteProject(Long projectId)
    {
        Long userId=authUtil.getCurrentUserId();

        return  projectMemberRepo.findRoleByProjectIdAndUserId(projectId,userId)
                .map(role -> role.getProjectPermissionSet().contains(ProjectPermission.DELETE)).orElse(false);
    }

    public boolean canViewProjectMembers(Long projectId)
    {
        Long userId=authUtil.getCurrentUserId();

        return  projectMemberRepo.findRoleByProjectIdAndUserId(projectId,userId)
                .map(role -> role.getProjectPermissionSet().contains(ProjectPermission.VIEW_MEMBERS)).orElse(false);
    }
    public boolean canManageProjectMembers(Long projectId)
    {
        Long userId=authUtil.getCurrentUserId();

        return  projectMemberRepo.findRoleByProjectIdAndUserId(projectId,userId)
                .map(role -> role.getProjectPermissionSet().contains(ProjectPermission.MANAGE_MEMBERS)).orElse(false);
    }

}
