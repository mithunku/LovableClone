package com.codingShuttle.project.Lovable.controller;

import com.codingShuttle.project.Lovable.dto.chat.ChatRequest;
import com.codingShuttle.project.Lovable.dto.chat.ChatResponse;
import com.codingShuttle.project.Lovable.service.AiGenerationService;
import com.codingShuttle.project.Lovable.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final AiGenerationService aiService;
    private final ChatService chatService;
    @PostMapping(value = "/api/chat/stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> straemChat(
            @RequestBody ChatRequest request
    )
    {
        return aiService.streamResponse(request.message(),request.projectId()).map(data -> ServerSentEvent.<String>builder()
                .data(data).build());
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<List<ChatResponse>> getMessage(@PathVariable Long projectId)
    {
        return ResponseEntity.ok(chatService.getProjectChatHistory(projectId));
    }
}
