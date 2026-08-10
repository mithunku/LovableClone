package com.codingShuttle.project.Lovable.service.impl;

import com.codingShuttle.project.Lovable.Mapper.ProjectMapper;
import com.codingShuttle.project.Lovable.Repository.ProjectMemberRepository;
import com.codingShuttle.project.Lovable.Repository.ProjectRepository;
import com.codingShuttle.project.Lovable.Repository.SubscriptionRepository;
import com.codingShuttle.project.Lovable.Repository.UserRepository;
import com.codingShuttle.project.Lovable.dto.project.ProjectRequest;
import com.codingShuttle.project.Lovable.dto.project.ProjectResponse;
import com.codingShuttle.project.Lovable.dto.project.ProjectSummaryResponse;
import com.codingShuttle.project.Lovable.entity.*;
import com.codingShuttle.project.Lovable.enums.ProjectRole;
import com.codingShuttle.project.Lovable.error.BadRequestException;
import com.codingShuttle.project.Lovable.error.ResourceNotFoundException;
import com.codingShuttle.project.Lovable.security.AuthUtil;
import com.codingShuttle.project.Lovable.service.ProjectService;
import com.codingShuttle.project.Lovable.service.ProjectTemplateService;
import com.codingShuttle.project.Lovable.service.SubscriptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ProjectServiceImpl implements ProjectService {

    private final UserRepository userRepo;
    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepo;
    private final AuthUtil authUtil;
    private final ProjectMemberRepository projectMemberRepository;
    private final SubscriptionService subscriptionService;
    private final ProjectTemplateService projectTemplateService;
    @Override
    public List<ProjectSummaryResponse> getUserProjects() {
        Long userId=authUtil.getCurrentUserId();
        List<Project> projects = projectRepo.findAllProjectOfUser(userId);
        List<ProjectSummaryResponse> projectReponses=projects.stream().map(projectMapper::toProjectSummaryResponse).collect(Collectors.toList());
        return projectReponses;
    }

    @Override
    @PreAuthorize("@security.canViewProject(#projectId)")
    public ProjectResponse getUserProjectsById(Long projectId)
    {
        Long userId=authUtil.getCurrentUserId();
        Project project =projectRepo.findProjectByIdAndUserId(userId,projectId).orElseThrow(()-> new BadRequestException("User not found"));
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request) {
        Long userId=authUtil.getCurrentUserId();
        User user=userRepo.findById(userId).orElseThrow();

        if(!subscriptionService.canCreateProject())
        {
            throw new BadRequestException("Number Of Projects Exceeded");
        }


        Project project=Project.builder()
                .name(request.name())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .isPublic(false)
                .build();
        project=projectRepo.save(project);
        ProjectMemberId projectMemberId=new ProjectMemberId(project.getId(), userId);
        ProjectMember projectMember=ProjectMember.builder()
                .id(projectMemberId)
                .projectRole(ProjectRole.OWNER)
                .project(project)
                .acceptedAt(Instant.now())
                .invitedAt(Instant.now())
                .user(user)
                .build();

        projectMemberRepository.save(projectMember);
        projectTemplateService.initilaizeProjectFromTemplate(project.getId());
        return projectMapper.toProjectResponse(project);
    }

    @Override
    @PreAuthorize("@security.canEditProject(#id)")
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Long userId=authUtil.getCurrentUserId();
        Project project =projectRepo.findProjectByIdAndUserId(userId,id).orElseThrow(()-> new ResourceNotFoundException("User or Project",userId.toString()));
        project.setName(request.name());
        project.setUpdatedAt(Instant.now());
        project=projectRepo.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    @PreAuthorize("@security.canDeleteProject(#id)")
    public void softDelete(Long id) {
        Long userId=authUtil.getCurrentUserId();
        Project project =projectRepo.findProjectByIdAndUserId(userId,id).orElseThrow();


        project.setDeletedAt(Instant.now());
        projectRepo.save(project);


    }
}
