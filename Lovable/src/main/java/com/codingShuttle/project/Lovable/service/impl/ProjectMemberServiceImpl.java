package com.codingShuttle.project.Lovable.service.impl;

import com.codingShuttle.project.Lovable.Mapper.ProjectMemberMapper;
import com.codingShuttle.project.Lovable.Repository.ProjectMemberRepository;
import com.codingShuttle.project.Lovable.Repository.ProjectRepository;
import com.codingShuttle.project.Lovable.Repository.UserRepository;
import com.codingShuttle.project.Lovable.dto.member.InviteMemberRequest;
import com.codingShuttle.project.Lovable.dto.member.MemberResponse;
import com.codingShuttle.project.Lovable.dto.member.UpdateRoleRequest;
import com.codingShuttle.project.Lovable.entity.Project;
import com.codingShuttle.project.Lovable.entity.ProjectMember;
import com.codingShuttle.project.Lovable.entity.ProjectMemberId;
import com.codingShuttle.project.Lovable.entity.User;
import com.codingShuttle.project.Lovable.security.AuthUtil;
import com.codingShuttle.project.Lovable.service.ProjectMemberService;
import com.codingShuttle.project.Lovable.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectMemberMapper memberMapper;
    private final ProjectMemberRepository projMemberRepo;
    private final ProjectRepository projRepo;
    private final UserRepository userRepo;
    private final AuthUtil authUtil;

    @Override
    @PreAuthorize("@security.canViewProjectMembers(#projectId)")
    public List<MemberResponse> getMembers(Long projectId) {

//        Project project = projRepo.findProjectByIdAndUserId(userId, projectId).orElseThrow();
        List<MemberResponse> memberResponseList = new ArrayList<MemberResponse>();



        memberResponseList.addAll(
                projMemberRepo.findByIdProjectId(projectId)
                        .stream()
                        .map(memberMapper::toMemberResponse)
                        .toList()
        );


        return memberResponseList;
    }

    @Override
    @PreAuthorize("@security.canManageProjectMembers(#projectId)")

    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request) {
        //here userId is id of user who is inviting this member
        //we will use email in request to get user;
        Long userId=authUtil.getCurrentUserId();
        Project project = projRepo.findProjectByIdAndUserId(projectId,userId).orElseThrow();

        User invitee = userRepo.findByUsername(request.username()).orElseThrow();
        if(invitee.getId().equals(userId))
        {
            throw new RuntimeException("Not allowed");
        }
        ProjectMemberId projectMemberId=new ProjectMemberId();
        projectMemberId.setProjectId(projectId);
        projectMemberId.setUserId(invitee.getId());

        if(projMemberRepo.existsById(projectMemberId))
        {
            throw new RuntimeException("cannot invite again");
        }
        ProjectMember projectmember = ProjectMember.builder()
                .id(projectMemberId)
                .projectRole(request.role())
                .user(invitee)
                .project(project)
                .invitedAt(Instant.now())
                .build();
          ProjectMember invitedMember =projMemberRepo.save(projectmember);

        return memberMapper.toMemberResponse(invitedMember);
    }

    @Override
    public MemberResponse updateMemberRole(Long projectId,  Long memberId, UpdateRoleRequest request) {
        Long userId=authUtil.getCurrentUserId();
        Project project = projRepo.findProjectByIdAndUserId(projectId,userId).orElseThrow();


        ProjectMemberId projectMemberId=new ProjectMemberId();
        projectMemberId.setProjectId(projectId);
        projectMemberId.setUserId(memberId);


        ProjectMember member=projMemberRepo.findById(projectMemberId).orElseThrow();

        member.setProjectRole(request.role());
        projMemberRepo.save(member);

        return memberMapper.toMemberResponse(member);
    }

    @Override
    public MemberResponse removeProjectMember(Long projectId,  Long memberId) {
        Long userId=authUtil.getCurrentUserId();
        Project project = projRepo.findProjectByIdAndUserId(projectId,userId).orElseThrow();


        ProjectMemberId projectMemberId=new ProjectMemberId();
        projectMemberId.setProjectId(projectId);
        projectMemberId.setUserId(memberId);

        if(!projMemberRepo.existsById(projectMemberId))
        {
            throw new RuntimeException("Member Not found");
        }

        projMemberRepo.deleteById(projectMemberId);
        return null;
    }
}
