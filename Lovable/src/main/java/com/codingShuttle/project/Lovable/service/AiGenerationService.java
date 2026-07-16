package com.codingShuttle.project.Lovable.service;

import com.stripe.net.HttpHeaders;
import reactor.core.publisher.Flux;

public interface AiGenerationService {
    Flux<String> streamResponse(String message, Long aLong);

}
