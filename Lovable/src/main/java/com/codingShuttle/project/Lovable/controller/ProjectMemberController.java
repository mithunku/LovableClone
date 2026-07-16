package com.codingShuttle.project.Lovable.controller;


import com.codingShuttle.project.Lovable.dto.member.InviteMemberRequest;
import com.codingShuttle.project.Lovable.dto.member.MemberResponse;
import com.codingShuttle.project.Lovable.dto.member.UpdateRoleRequest;
import com.codingShuttle.project.Lovable.service.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/members")
@RequiredArgsConstructor
public class ProjectMemberController {

private final ProjectMemberService projectMemberService;

@GetMapping
public ResponseEntity<List<MemberResponse>> getMembers(@PathVariable Long projectId){

    return ResponseEntity.ok(projectMemberService.getMembers(projectId));
}

@PostMapping
    public  ResponseEntity<MemberResponse> inviteMember(
            @PathVariable Long projectId,
            @RequestBody @Valid InviteMemberRequest request
)
{

    return ResponseEntity.status(HttpStatus.CREATED).body(projectMemberService.inviteMember(projectId,request));
}

@PatchMapping("/{memberId}")
    public ResponseEntity<MemberResponse> updateMemberRole(@PathVariable Long memberId,@PathVariable Long projectId,@RequestBody @Valid UpdateRoleRequest request)
{



    return ResponseEntity.ok(projectMemberService.updateMemberRole(projectId,memberId,request));
}

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> removeProjectMember(@PathVariable Long memberId,@PathVariable Long projectId)
    {


        projectMemberService.removeProjectMember(projectId,memberId);
        return ResponseEntity.noContent().build();
    }


}
