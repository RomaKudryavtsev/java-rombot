package com.rombot.rombot.service;

import com.rombot.rombot.dto.request.MessageLanguage;
import com.rombot.rombot.dto.request.MessageTemplate;
import com.rombot.rombot.dto.request.SendMessageRequest;
import com.rombot.rombot.dto.response.SendMessageResponse;
import com.rombot.rombot.repo.TargetRepo;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendingService {
    final static String MESSAGING_PRODUCT = "whatsapp";
    final static String MESSAGE_TYPE = "template";
    final static String BASE_URL = "https://graph.facebook.com/v17.0/109129788925398/messages";
    @Value("${lang.code}")
    String languageCode;
    @Value("${auth.token}")
    String token;
    @Autowired
    TargetRepo targetRepo;

    private final WebClient sendingClient = WebClient.builder()
            .baseUrl(BASE_URL)
            .build();


    public List<SendMessageResponse> sendMessage(String templateName) {
        return targetRepo.findAll().stream()
                .map(target -> sendingClient
                        .post()
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                        .bodyValue(buildSendRequest(target.getPhone(), templateName))
                        .retrieve().bodyToMono(SendMessageResponse.class).block())
                .collect(Collectors.toList());
    }

    private SendMessageRequest buildSendRequest(String targetPhoneNumber, String templateName) {
        SendMessageRequest request = new SendMessageRequest();
        request.setMessaging_product(MESSAGING_PRODUCT);
        request.setTo(targetPhoneNumber);
        request.setType(MESSAGE_TYPE);
        request.setTemplate(buildMessageTemplate(templateName));
        return request;
    }

    private MessageTemplate buildMessageTemplate(String templateName) {
        MessageTemplate template = new MessageTemplate();
        template.setName(templateName);
        template.setLanguage(getLanguage());
        return template;
    }

    private MessageLanguage getLanguage() {
        MessageLanguage language = new MessageLanguage();
        language.setCode(languageCode);
        return language;
    }
}