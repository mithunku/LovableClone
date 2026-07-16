package com.codingShuttle.project.Lovable.entity;

import com.codingShuttle.project.Lovable.enums.MessageRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumns(
            {
                    @JoinColumn(name = "project_id",referencedColumnName = "project_Id",nullable = false),
                    @JoinColumn(name = "user_id",referencedColumnName = "user_id",nullable = false)
            }
    )
    ChatSession chatSession;

    @Column(columnDefinition = "text",nullable = false)
    String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    MessageRole messageRole;
    String toolCalls;
    String tokensUsed;

    @CreationTimestamp
    Instant createdAt;
}
