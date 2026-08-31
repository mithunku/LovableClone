package com.codingShuttle.project.Lovable.dto.chat;

import com.codingShuttle.project.Lovable.entity.ChatMessage;
import com.codingShuttle.project.Lovable.enums.ChatEventType;
import jakarta.persistence.*;
import lombok.Data;


public record ChatEventResponse (

    Long id,


    ChatMessage chatMessage,


    ChatEventType chatEventType,

    //used to define sequence in with event occur like eg "thinking" "file edited"

    Integer sequenceOrder,


    String content,

    //this can be used in case the chat event is file edit
    String filePath,

    //logs of tools

    String metadata
)
{

}
