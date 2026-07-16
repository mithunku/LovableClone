package com.codingShuttle.project.Lovable.error;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex)
    {
        ApiError apiError=new ApiError(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(apiError.status()).body(apiError);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex)
    {
        ApiError apiError=new ApiError(HttpStatus.NOT_FOUND,ex.getResourceName() + " with ID " + ex.getResourceId() + " not found");
        return ResponseEntity.status(apiError.status()).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleVlidartionErrors(MethodArgumentNotValidException ex)
    {
        var errors =ex.getBindingResult().getFieldErrors().stream().map((error)->{
            ApiError.ApiFieldError apiFieldError=new ApiError.ApiFieldError(error.getField(),error.getDefaultMessage());
            return apiFieldError;
        });
        ApiError apiError=new ApiError(HttpStatus.BAD_REQUEST ,"Input validation failed", errors.toList());
        return ResponseEntity.status(apiError.status()).body(apiError);

    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex)
    {
        ApiError apiError=new ApiError(HttpStatus.UNAUTHORIZED,ex.getMessage());
        return ResponseEntity.status(apiError.status()).body(apiError);
    }


    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(JwtException ex)
    {
        ApiError apiError=new ApiError(HttpStatus.UNAUTHORIZED,"Invalid Jwt Token: "+ex.getMessage());
        return ResponseEntity.status(apiError.status()).body(apiError);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(JwtException ex)
    {
        ApiError apiError=new ApiError(HttpStatus.FORBIDDEN,"Access denied: Insufficient permissions"+ex.getMessage());
        return ResponseEntity.status(apiError.status()).body(apiError);
    }



}
