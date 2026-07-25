package com.codingShuttle.project.Lovable.llm.advisors;

import com.codingShuttle.project.Lovable.dto.files.FileNode;
import com.codingShuttle.project.Lovable.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class FileTreeContextAdvisor implements StreamAdvisor {

    private final ProjectFileService projectFileService;

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        //gets our prompt

        Map<String ,Object> context=chatClientRequest.context();
        Long projectId=Long.parseLong(context.getOrDefault("projectId",0).toString());

        //here we are adding the file tree content to our prompt
        ChatClientRequest augementedChatClientRequest= augementRequestWithFileTree(chatClientRequest,projectId);



        /// here we are passing the chatrequest to next advsior
        return streamAdvisorChain.nextStream(augementedChatClientRequest);
    }

    private ChatClientRequest augementRequestWithFileTree(ChatClientRequest request,Long projectId){

        List<Message> incommingMessages= request.prompt().getInstructions();

        //fetch system message
        Message systemMessage=incommingMessages.stream().filter(m->m.getMessageType()== MessageType.SYSTEM)
                .findFirst()
                .orElse(null);

        //fetch user Message
        List<Message> userMessage=incommingMessages.stream().filter(m->m.getMessageType()==MessageType.USER)
                .toList();

        List<Message> allMessages=new ArrayList<>();

        if(systemMessage!=null)
        {
            allMessages.add(systemMessage);
        }

        //build prompt in this way systemprompt+userprompt+new file tree context
        List<FileNode> filepaths =projectFileService.getFileTree(projectId);
        String fileTreeContext= "\n\n----FILE_TREE-----\n\n"+filepaths.toString();

        //Using a SystemMessage tells the model:
        //
        //"This information comes from the application. Use it as context."
        //if we use "new UserMessgae(fielTreeContext)" then llm wil think user has typed this prompt
        allMessages.add(new SystemMessage(fileTreeContext));
        allMessages.addAll(userMessage);

        //option are other setting like tempertaure what model ur using
        //this mutate does not modify the request it will create a new chatrequest
        return request.mutate().prompt(new Prompt(allMessages,request.prompt().getOptions())).build();
    }

    @Override
    public String getName() {
        return "FileTreeContextAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
