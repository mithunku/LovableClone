package com.codingShuttle.project.Lovable.service;

import com.codingShuttle.project.Lovable.dto.auth.AuthResponse;
import com.codingShuttle.project.Lovable.dto.auth.LoginRequest;
import com.codingShuttle.project.Lovable.dto.auth.SignUpRequest;

public interface AuthService {
    AuthResponse signup(SignUpRequest signUpRequest);

    AuthResponse login(LoginRequest request);
}
