package com.codingShuttle.project.Lovable.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    public ChatClient chatClient(ChatClient.Builder builder)
    {
        return builder
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                .build();
    }

}
