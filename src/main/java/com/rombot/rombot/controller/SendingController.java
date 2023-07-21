package com.rombot.rombot.controller;

import com.rombot.rombot.dto.response.SendMessageResponse;
import com.rombot.rombot.service.SendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/send")
@RequiredArgsConstructor
public class SendingController {
    private final SendingService service;

    @PostMapping
    public List<SendMessageResponse> sendMessage(@RequestParam("template") String templateName) {
        return service.sendMessage(templateName);
    }
}
