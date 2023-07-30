package com.rombot.rombot.controller;

import com.rombot.rombot.dto.ResultDto;
import com.rombot.rombot.service.ISendingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rombot")
@RequiredArgsConstructor
@Slf4j
public class SendingController {
    private final ISendingService service;

    @Tag(name = "START", description = "Start sending messages")
    @Operation(
            summary = "Enable sending message stream",
            description = "This will subscribe you on the reactive stream. " +
                    "In case of using official API - template is the name of template message. " +
                    "In case of 3rd party API - template is simply the message body."
    )
    @PostMapping(value = "/send", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ResultDto> sendMessage(@RequestParam("template") String template,
                                       @RequestParam(name = "number", required = false) Integer numberOfMessages,
                                       @RequestParam(name = "delay_sec", required = false, defaultValue = "3") Integer delay) {
        return service.startSending(template, numberOfMessages, delay)
                .doOnSubscribe(subscription -> log.info("Start sending"))
                .doOnComplete(() -> log.info("Finished sending"));
    }

    @Tag(name = "STOP", description = "Stop sending messages")
    @Operation(
            summary = "Disable sending message stream",
            description = "This will cancel the reactive stream"
    )
    @PostMapping(value = "/cancel")
    public Mono<Void> cancelSendMessage() {
        return service.cancelSending();
    }
}
