package com.codingShuttle.project.Lovable.Mapper;

import com.codingShuttle.project.Lovable.dto.auth.UserProfileResponse;
import com.codingShuttle.project.Lovable.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring") //here we are specifying which framework we are using
public interface UserMapper {
    UserProfileResponse toUserProfileResponse(User user);
}
