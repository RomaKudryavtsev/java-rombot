package com.rombot.rombot.controller;

import com.rombot.rombot.dto.ResultDto;
import com.rombot.rombot.service.SendingService;
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

@RestController
@RequestMapping("/send")
@Tag(name = "sending_template", description = "Enable sending message stream")
@RequiredArgsConstructor
@Slf4j
public class SendingController {
    private final SendingService service;

    @Operation(
            summary = "Enable sending message stream",
            description = "This will subscribe you on the reactive stream"
    )
    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ResultDto> sendMessage(@RequestParam("template") String templateName) {
        return service.startSending(templateName)
                .doOnSubscribe(subscription -> log.info("Start sending"))
                .doOnComplete(() -> log.info("Finished sending"));
    }
}
