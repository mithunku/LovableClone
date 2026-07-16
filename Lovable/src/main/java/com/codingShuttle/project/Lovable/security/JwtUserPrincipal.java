package com.codingShuttle.project.Lovable.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public record JwtUserPrincipal(Long id, String username, List<GrantedAuthority> authorities) {

}
