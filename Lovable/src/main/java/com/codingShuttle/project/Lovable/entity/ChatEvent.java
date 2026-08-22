package com.codingShuttle.project.Lovable.entity;

import com.codingShuttle.project.Lovable.enums.ChatEventType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "chat_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    ChatMessage chatMessage;

    @Enumerated(EnumType.STRING)
            @Column(nullable = false)
    ChatEventType chatEventType;

    //used to define sequence in with event occur like eg "thinking" "file edited"
    @Column(nullable = false)
    Integer sequenceOrder;

    @Column(columnDefinition = "text")
    String content;

    //this can be used in case the chat event is file edit
    String filePath;

    //logs of tools
    @Column(columnDefinition = "text")
    String metadata;


}
