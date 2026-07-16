package com.codingShuttle.project.Lovable.service.impl;

import com.codingShuttle.project.Lovable.Mapper.UserMapper;
import com.codingShuttle.project.Lovable.Repository.UserRepository;
import com.codingShuttle.project.Lovable.dto.auth.AuthResponse;
import com.codingShuttle.project.Lovable.dto.auth.LoginRequest;
import com.codingShuttle.project.Lovable.dto.auth.SignUpRequest;
import com.codingShuttle.project.Lovable.entity.User;
import com.codingShuttle.project.Lovable.error.BadRequestException;
import com.codingShuttle.project.Lovable.error.ResourceNotFoundException;
import com.codingShuttle.project.Lovable.security.AuthUtil;
import com.codingShuttle.project.Lovable.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {

   UserRepository userRepo;
   PasswordEncoder passwordEncoder;
   UserMapper userMapper;
   AuthenticationManager authManager;
   AuthUtil authUtil;
    @Override
    public AuthResponse signup(SignUpRequest signUpRequest) {

        System.out.println("entered signup service");

        userRepo.findByUsername(signUpRequest.username()).ifPresent((user)->
        {
            throw new BadRequestException("User with username "+ signUpRequest.username() + " already exists");
        }
    );
        User user=new User();
        user.setUsername(signUpRequest.username());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        user.setAvatarUrl("Gojo Senpai");
        user.setName(signUpRequest.name());
        User savedUser= userRepo.save(user);
        return new AuthResponse("wdwdwdwd",userMapper.toUserProfileResponse(savedUser));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication=authManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(),request.password()));
        User user= (User) authentication.getPrincipal();
        String token=authUtil.generateJwtToken(user);

//        if(authentication.isAuthenticated())
//        {
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }

        return new AuthResponse(token,userMapper.toUserProfileResponse(user));

    }
}
