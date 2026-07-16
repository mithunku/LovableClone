package com.codingShuttle.project.Lovable.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

//records creats getter ,toSTring ,equsls,al all arg cinstructors
public record ApiError(
        HttpStatus status,
        String message,
        Instant timestamp,
       @JsonInclude(JsonInclude.Include.NON_NULL) List<ApiFieldError> Errors
) {
public  ApiError(HttpStatus status , String message)
{
    this(status,message,Instant.now(),null);
}

    public  ApiError(HttpStatus status ,String message,List<ApiFieldError> apifieldError)
    {
        this(status,message,Instant.now(),apifieldError);
    }


public record ApiFieldError(
        String field,
        String message
)
{

}
}
