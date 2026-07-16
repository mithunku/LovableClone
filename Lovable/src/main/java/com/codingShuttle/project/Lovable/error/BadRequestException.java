package com.codingShuttle.project.Lovable.error;


public class BadRequestException extends RuntimeException{

String message;
    public BadRequestException(String message)
    {
        super(message);
        this.message=message;


    }
}
