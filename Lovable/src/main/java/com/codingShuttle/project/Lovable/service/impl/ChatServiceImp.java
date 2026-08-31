package com.codingShuttle.project.Lovable.service.impl;

import com.codingShuttle.project.Lovable.Mapper.ChatMapper;
import com.codingShuttle.project.Lovable.Repository.ChatMessageRepository;
import com.codingShuttle.project.Lovable.Repository.ChatSessionRepositoty;
import com.codingShuttle.project.Lovable.dto.chat.ChatResponse;
import com.codingShuttle.project.Lovable.entity.ChatMessage;
import com.codingShuttle.project.Lovable.entity.ChatSession;
import com.codingShuttle.project.Lovable.entity.ChatSessionId;
import com.codingShuttle.project.Lovable.security.AuthUtil;
import com.codingShuttle.project.Lovable.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImp implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final AuthUtil authUtil;
    private final ChatSessionRepositoty chatSessionRepositoty;
    private final ChatMapper chatMapper;
    @Override
    public List<ChatResponse> getProjectChatHistory(Long projectId) {
       Long userId=authUtil.getCurrentUserId();
        ChatSession chatSession=chatSessionRepositoty.getReferenceById(new ChatSessionId(projectId,userId));
        List<ChatMessage> chatMessages =chatMessageRepository.findByChatSession(chatSession);
        return chatMapper.fromListOfChatMessage(chatMessages);
    }
}
