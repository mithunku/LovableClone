package com.codingShuttle.project.Lovable.Mapper;

import com.codingShuttle.project.Lovable.dto.member.MemberResponse;
import com.codingShuttle.project.Lovable.entity.ProjectMember;
import com.codingShuttle.project.Lovable.entity.User;
import com.codingShuttle.project.Lovable.enums.ProjectRole;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-16T21:44:00+0530",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class ProjectMemberMapperImpl implements ProjectMemberMapper {

    @Override
    public MemberResponse toMemberResponse(User user) {
        if ( user == null ) {
            return null;
        }

        Long userId = null;
        String username = null;
        String name = null;
        String avatarUrl = null;

        userId = user.getId();
        username = user.getUsername();
        name = user.getName();
        avatarUrl = user.getAvatarUrl();

        ProjectRole projectRole = ProjectRole.OWNER;
        Instant invitedAt = null;

        MemberResponse memberResponse = new MemberResponse( userId, username, name, avatarUrl, projectRole, invitedAt );

        return memberResponse;
    }

    @Override
    public MemberResponse toMemberResponse(ProjectMember member) {
        if ( member == null ) {
            return null;
        }

        Long userId = null;
        String username = null;
        String name = null;
        ProjectRole projectRole = null;
        Instant invitedAt = null;

        userId = memberUserId( member );
        username = memberUserUsername( member );
        name = memberUserName( member );
        projectRole = member.getProjectRole();
        invitedAt = member.getInvitedAt();

        String avatarUrl = null;

        MemberResponse memberResponse = new MemberResponse( userId, username, name, avatarUrl, projectRole, invitedAt );

        return memberResponse;
    }

    private Long memberUserId(ProjectMember projectMember) {
        User user = projectMember.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }

    private String memberUserUsername(ProjectMember projectMember) {
        User user = projectMember.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getUsername();
    }

    private String memberUserName(ProjectMember projectMember) {
        User user = projectMember.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getName();
    }
}
