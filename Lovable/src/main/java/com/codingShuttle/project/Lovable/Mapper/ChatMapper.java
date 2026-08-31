package com.codingShuttle.project.Lovable.Mapper;

import com.codingShuttle.project.Lovable.dto.chat.ChatResponse;
import com.codingShuttle.project.Lovable.entity.ChatMessage;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring") //here we are specifying which framework we are using
public interface ChatMapper {

    List<ChatResponse> fromListOfChatMessage(List<ChatMessage> chatMessage);
}
