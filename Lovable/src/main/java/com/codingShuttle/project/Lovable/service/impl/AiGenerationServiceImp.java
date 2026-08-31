package com.codingShuttle.project.Lovable.service.impl;

import com.codingShuttle.project.Lovable.Repository.*;
import com.codingShuttle.project.Lovable.entity.*;
import com.codingShuttle.project.Lovable.enums.ChatEventType;
import com.codingShuttle.project.Lovable.enums.MessageRole;
import com.codingShuttle.project.Lovable.error.ResourceNotFoundException;
import com.codingShuttle.project.Lovable.llm.LllmResponseParser;
import com.codingShuttle.project.Lovable.llm.PromptUtils;
import com.codingShuttle.project.Lovable.llm.advisors.FileTreeContextAdvisor;
import com.codingShuttle.project.Lovable.llm.tools.CodeGenerationTools;
import com.codingShuttle.project.Lovable.security.AuthUtil;
import com.codingShuttle.project.Lovable.service.AiGenerationService;
import com.codingShuttle.project.Lovable.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiGenerationServiceImp implements AiGenerationService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ChatClient chatClient;
    private final AuthUtil authUtil;
    private static final Pattern FILE_TAG_PATTERN=Pattern.compile("<file path=\"([^\"]+)\">(.*?)</file>",Pattern.DOTALL);
    private final ProjectFileService projectFileService;
    private final FileTreeContextAdvisor fileTreeContextAdvisor;
    private final ChatSessionRepositoty chatSessionRepositoty;
    private final LllmResponseParser lllmResponseParser;

    private final ChatEventRepository chatEventRepository;
    @Override
    @PreAuthorize("@security.canEditProject(#projectId)")
    public Flux<String> streamResponse(String userMessage, Long projectId) {
        Long userId = authUtil.getCurrentUserId();
        ChatSession chatSession=createChatSessionIfNotExists(projectId,userId);
        createChatSessionIfNotExists(projectId, userId);
        //context
        Map<String, Object> advisorsParams = Map.of(
                "userId", userId,
                "projectId", projectId

                );

        StringBuilder fullResponseBuffer =new StringBuilder();
        CodeGenerationTools codeGenerationTools =new CodeGenerationTools(projectFileService,projectId);
        return chatClient.prompt().system(PromptUtils.CODE_GENERATION_SYSTEM_PROMPT)
                .user(userMessage)
                .tools(codeGenerationTools)
                .advisors(advisorSpec -> {
                    advisorSpec.params(advisorsParams);
                    advisorSpec.advisors(fileTreeContextAdvisor);

                })
                .stream()
                .chatResponse()
                .doOnNext(response -> {
                    fullResponseBuffer.append(response.getResult().getOutput().getText());
                }).doOnComplete(() -> {
                    SecurityContext context = SecurityContextHolder.getContext();
                    //this runs in sepaaret thread
                    log.info("Streaming completed");
                    Schedulers.boundedElastic().schedule(()->{
                       // SecurityContextHolder.setContext(context); // restore on new thread
                       // try {
                            parseAndSaveFile(fullResponseBuffer.toString(), projectId);
                            finilizeChats(userMessage,chatSession,fullResponseBuffer.toString(),projectId);
                       // } finally {
                          //  SecurityContextHolder.clearContext(); // avoid leaking into pooled thread
                       // }

                    });

                        }
                ).doOnError(error -> log.error("Error during streaming for projects"))
                .map(response -> Objects.requireNonNull(response.getResult().getOutput().getText()));

        }

        private void finilizeChats(String userMessage,ChatSession chatSession,String fullText,Long projectId){

        chatMessageRepository.save(ChatMessage.builder().chatSession(chatSession).messageRole(MessageRole.USER).content(userMessage).build());
        ChatMessage assistantChatMessage= ChatMessage.builder().content("Assistant message here...").chatSession(chatSession).messageRole(MessageRole.ASSISTANT).build();

        chatMessageRepository.save(assistantChatMessage);
        List<ChatEvent> chatEventList= lllmResponseParser.parseChatEvents(fullText,assistantChatMessage);

        chatEventList.stream().filter(e->e.getChatEventType()== ChatEventType.FILE_EDIT)
                .forEach(e->projectFileService.saveFile(projectId,e.getFilePath(),e.getContent()));

       chatEventRepository.saveAll(chatEventList);

        }

    private void parseAndSaveFile(String fullResponseBuffer, Long projectId) {
        Matcher matcher=FILE_TAG_PATTERN.matcher(fullResponseBuffer);

        while(matcher.find())

        {
            String filePath=matcher.group(1);
            String fileContent=matcher.group(2).trim();

            projectFileService.saveFile(projectId,filePath,fileContent);

        }
    }

    private ChatSession createChatSessionIfNotExists (Long projectId, Long userId){
        ChatSessionId chatSessionId=new ChatSessionId(projectId,userId);
        ChatSession chatSession = chatSessionRepositoty.findById(chatSessionId).orElse(null);
        if(chatSession==null)
        {
            Project project= projectRepository.findById(projectId).orElseThrow(()-> new ResourceNotFoundException("project",Long.toString(projectId)));
             User user= userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user",Long.toString(userId)));
            ChatSession newChatSession = ChatSession.builder().user(user)
                    .project(project).id(chatSessionId).build();

           chatSession=chatSessionRepositoty.save(newChatSession);



        }

        return chatSession;
        }

}
