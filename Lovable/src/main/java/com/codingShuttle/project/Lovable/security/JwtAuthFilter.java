package com.codingShuttle.project.Lovable.security;

import com.codingShuttle.project.Lovable.service.impl.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthUtil authUtil;
    private final UserServiceImpl userService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    // NEW — persists context so it survives ASYNC redispatch during streaming
    private final SecurityContextRepository securityContextRepository =
            new RequestAttributeSecurityContextRepository();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = header.substring(7);
            JwtUserPrincipal jwtUserPrincipal = authUtil.parseJwt(token);

            if (jwtUserPrincipal != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(jwtUserPrincipal, null, jwtUserPrincipal.authorities());

                // build a fresh context instead of mutating the holder's existing one
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);

                // CRITICAL: save it, so SecurityContextHolderFilter can reload it
                // on every subsequent ASYNC dispatch during Flux streaming
                securityContextRepository.saveContext(context, request, response);
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}