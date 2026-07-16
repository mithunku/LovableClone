package com.codingShuttle.project.Lovable.controller;

import com.codingShuttle.project.Lovable.dto.chat.ChatRequest;
import com.codingShuttle.project.Lovable.service.AiGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final AiGenerationService aiService;
    @PostMapping(value = "/api/chat/stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> straemChat(
            @RequestBody ChatRequest request
    )
    {
        return aiService.streamResponse(request.message(),request.projectId()).map(data -> ServerSentEvent.<String>builder()
                .data(data).build());
    }
}
