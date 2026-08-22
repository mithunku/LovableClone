package com.codingShuttle.project.Lovable.Repository;

import com.codingShuttle.project.Lovable.entity.ChatMessage;
import com.codingShuttle.project.Lovable.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {

    //here we use fetch to avoid N+1 db queries fetch will get all data in one query
    //Give me distinct ChatMessages (m) from the ChatMessage entity, left join their events, fetch those events immediately, only for messages belonging to this ChatSession, and order the messages by creation time and events by sequence order.
    @Query("""
SELECT DISTINCT m FROM ChatMessage m
LEFT JOIN FETCH m.events e
WHERE m.chatSession= :chatSession
ORDER BY m.createdAt ASC, e.sequenceOrder ASC
""")
    List<ChatMessage> findByChatSession(ChatSession chatSession);

}
