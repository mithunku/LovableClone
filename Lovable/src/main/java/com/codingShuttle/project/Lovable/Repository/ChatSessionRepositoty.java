package com.codingShuttle.project.Lovable.Repository;

import com.codingShuttle.project.Lovable.entity.ChatSession;
import com.codingShuttle.project.Lovable.entity.ChatSessionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatSessionRepositoty extends JpaRepository<ChatSession, ChatSessionId> {
    ChatSession findByUserAndProject(Long userId, Long projectId);
}
