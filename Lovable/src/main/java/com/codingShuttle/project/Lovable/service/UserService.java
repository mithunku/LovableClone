package com.codingShuttle.project.Lovable.service;
import com.codingShuttle.project.Lovable.dto.auth.UserProfileResponse;

public interface UserService {
    UserProfileResponse getProfile(Long userId);
}
