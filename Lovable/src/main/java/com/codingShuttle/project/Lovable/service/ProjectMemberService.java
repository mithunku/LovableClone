package com.codingShuttle.project.Lovable.service;

import com.codingShuttle.project.Lovable.dto.member.InviteMemberRequest;
import com.codingShuttle.project.Lovable.dto.member.MemberResponse;
import com.codingShuttle.project.Lovable.dto.member.UpdateRoleRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectMemberService {
    List<MemberResponse> getMembers(Long projectId);

    MemberResponse inviteMember(Long projectId, InviteMemberRequest request);

    MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateRoleRequest request);

    MemberResponse removeProjectMember(Long projectId, Long memberId);
}
