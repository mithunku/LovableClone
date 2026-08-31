package com.codingShuttle.project.Lovable.Repository;

import com.codingShuttle.project.Lovable.entity.ChatEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatEventRepository extends JpaRepository<ChatEvent,Long> {
}
