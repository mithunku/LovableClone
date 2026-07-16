package com.codingShuttle.project.Lovable.Mapper;

import com.codingShuttle.project.Lovable.dto.member.MemberResponse;
import com.codingShuttle.project.Lovable.dto.project.ProjectResponse;
import com.codingShuttle.project.Lovable.entity.Project;
import com.codingShuttle.project.Lovable.entity.ProjectMember;
import com.codingShuttle.project.Lovable.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {

    @Mapping(target = "userId" ,source = "id")
    @Mapping(target = "projectRole" ,constant = "OWNER")
    MemberResponse toMemberResponse(User user);


    @Mapping(target = "userId" ,source = "user.id")
    @Mapping(target = "username" , source = "user.username")
    @Mapping(target = "name" ,source = "user.name")
    MemberResponse toMemberResponse(ProjectMember member);

}
