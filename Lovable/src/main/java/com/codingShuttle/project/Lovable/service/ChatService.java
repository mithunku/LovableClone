package com.codingShuttle.project.Lovable.service;

import com.codingShuttle.project.Lovable.dto.chat.ChatResponse;

import java.util.List;

public interface ChatService {
    List<ChatResponse> getProjectChatHistory(Long projectId);
}
