package com.codingShuttle.project.Lovable.Mapper;

import com.codingShuttle.project.Lovable.dto.auth.UserProfileResponse;
import com.codingShuttle.project.Lovable.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-16T21:44:00+0530",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserProfileResponse toUserProfileResponse(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String username = null;
        String name = null;

        id = user.getId();
        username = user.getUsername();
        name = user.getName();

        UserProfileResponse userProfileResponse = new UserProfileResponse( id, username, name );

        return userProfileResponse;
    }
}
