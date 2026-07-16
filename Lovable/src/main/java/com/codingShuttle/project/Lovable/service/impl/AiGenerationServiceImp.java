package com.codingShuttle.project.Lovable.service.impl;

import com.codingShuttle.project.Lovable.service.AiGenerationService;
import reactor.core.publisher.Flux;

public class AiGenerationServiceImp implements AiGenerationService {
    @Override
    public Flux<String> streamResponse(String message, Long aLong) {
        return null;
    }
}
