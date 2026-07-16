package com.codingShuttle.project.Lovable.security;

import com.codingShuttle.project.Lovable.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

@Service

public class AuthUtil {

    @Value("${secret.key}")
    private  String secretKey;

    private SecretKey getSecretKey()
    {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
    public String generateJwtToken(User user)
    {
        return Jwts.builder().signWith(getSecretKey())
                .subject(user.getUsername())
                .claim("userId",user.getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*60*60))
                .compact();

    }

    public JwtUserPrincipal parseJwt(String token)
    {
        //first create jwt.parser() and then use verify with function to vetify secretkey and the  build,and then parse it get cliams
        Claims claims= Jwts.parser().verifyWith(getSecretKey())
                .build().parseSignedClaims(token)
                .getPayload();

        Long id=claims.get("userId",Long.class);
        String username=claims.getSubject();

        JwtUserPrincipal jwtUserPrincipal=new JwtUserPrincipal(id,username,new ArrayList<>());
        return jwtUserPrincipal;

    }

public Long getCurrentUserId()
{
  Authentication auth= SecurityContextHolder.getContext().getAuthentication();
  if(auth==null || !(auth.getPrincipal() instanceof JwtUserPrincipal))
  {
      throw new AuthenticationCredentialsNotFoundException("No Jwt Found");
  }
  JwtUserPrincipal user= (JwtUserPrincipal) auth.getPrincipal();

  return user.id();
}
}
