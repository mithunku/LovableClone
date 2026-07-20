package com.codingShuttle.project.Lovable.service.impl;

import com.codingShuttle.project.Lovable.llm.PromptUtils;
import com.codingShuttle.project.Lovable.security.AuthUtil;
import com.codingShuttle.project.Lovable.service.AiGenerationService;
import com.codingShuttle.project.Lovable.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiGenerationServiceImp implements AiGenerationService {
    private final ChatClient chatClient;
    private final AuthUtil authUtil;
    private static final Pattern FILE_TAG_PATTERN=Pattern.compile("<file path=\"([^\"]+)\">(.*?)</file>",Pattern.DOTALL);
    private final ProjectFileService projectFileService;

    @Override
    @PreAuthorize("@security.canEditProject(#projectId)")
    public Flux<String> streamResponse(String userMessage, Long projectId) {
        Long userId = authUtil.getCurrentUserId();
        createChatSessionIfNotExists(projectId, userId);
        Map<String, Object> advisorsParams = Map.of(
                "userId", userId,
                "projectId", projectId

                );

        StringBuilder fullResponseBuffer =new StringBuilder();
        return chatClient.prompt().system(PromptUtils.CODE_GENERATION_SYSTEM_PROMPT)
                .user(userMessage)
                .advisors(advisorSpec -> {
                    advisorSpec.params(advisorsParams);

                })
                .stream()
                .chatResponse()
                .doOnNext(response -> {
                    fullResponseBuffer.append(response.getResult().getOutput().getText());
                }).doOnComplete(() -> {
                    //this runs in sepaaret thread
                    Schedulers.boundedElastic().schedule(()->{
                        parseAndSaveFile(fullResponseBuffer.toString(),projectId);
                    });

                        }
                ).doOnError(error -> log.error("Error during streaming for projects"))
                .map(response -> Objects.requireNonNull(response.getResult().getOutput().getText()));

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

    private void createChatSessionIfNotExists (Long projectId, Long userId){
        }

}
