package com.rombot.rombot.service;

import com.rombot.rombot.dto.ResultDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ISendingService<T> {
    Flux<ResultDto> startSending(String templateName, Integer numberOfMessages, Integer delay);

    Mono<Void> cancelSending();
}
