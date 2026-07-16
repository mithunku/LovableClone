package com.codingShuttle.project.Lovable.service.impl;

import com.codingShuttle.project.Lovable.Repository.UserRepository;
import com.codingShuttle.project.Lovable.dto.auth.UserProfileResponse;
import com.codingShuttle.project.Lovable.entity.User;
import com.codingShuttle.project.Lovable.error.ResourceNotFoundException;
import com.codingShuttle.project.Lovable.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService , UserDetailsService {
    @Override
    public UserProfileResponse getProfile(Long userId) {
        return null;
    }
    private  final UserRepository userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepo.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("User","1"));
        return user;
    }
}
