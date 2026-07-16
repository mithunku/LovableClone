package com.codingShuttle.project.Lovable.dto.auth;
//record makes this class immutable and it make all variable private adn final it cretas constructors ,toString,equals
public record AuthResponse(String token,UserProfileResponse user) {

}
