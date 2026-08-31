package com.codingShuttle.project.Lovable.dto.chat;

import com.codingShuttle.project.Lovable.entity.ChatEvent;
import com.codingShuttle.project.Lovable.entity.ChatSession;
import com.codingShuttle.project.Lovable.enums.MessageRole;


import java.time.Instant;
import java.util.List;

// here getters will be genearted no setter and all arg consuters and all fileds will be private and final
public record ChatResponse (

    Long id,
    ChatSession chatSession,
    String content,


    MessageRole messageRole,


    List<ChatEvent> events,
    String toolCalls,
    String tokensUsed,


    Instant createdAt
)
{

}
