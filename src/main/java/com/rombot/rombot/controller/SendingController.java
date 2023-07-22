package com.rombot.rombot.controller;

import com.rombot.rombot.dto.ResultDto;
import com.rombot.rombot.entity.Result;
import com.rombot.rombot.service.SendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/send")
@RequiredArgsConstructor
public class SendingController {
    private final SendingService service;

    @PostMapping
    public Flux<ResultDto> sendMessage(@RequestParam("template") String templateName) {
        return service.startSending(templateName);
    }
}
