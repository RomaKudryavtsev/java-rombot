package com.rombot.rombot.service.impl;

import com.rombot.rombot.dto.wa_api_request.MessageLanguage;
import com.rombot.rombot.dto.wa_api_request.MessageTemplate;
import com.rombot.rombot.dto.wa_api_request.SendMessageRequest;
import com.rombot.rombot.dto.wa_api_response.SendMessageResponse;
import com.rombot.rombot.entity.SourceContact;
import com.rombot.rombot.exception.SendMessageException;
import com.rombot.rombot.service.AbstractSendingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class SendingServiceOfficialApi extends AbstractSendingService<SendMessageResponse> {
    final static String MESSAGING_PRODUCT = "whatsapp";
    final static String MESSAGE_TYPE = "template";
    @Value("${rombot.lang.code}")
    String languageCode;
    @Value("${rombot.auth.token}")
    String token;

    //Official WhatsApp Business API
    //TODO: add check whether message was actually sent
    @Override
    protected Mono<SendMessageResponse> sendMessage(SourceContact sourceContact, String templateName) {
        return sendingClient
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .bodyValue(setSendRequest(sourceContact.getPhone(), templateName))
                .retrieve().bodyToMono(SendMessageResponse.class)
                .doOnNext(sendMessageResponse -> log.info(Arrays.toString(sendMessageResponse.getContacts())))
                .onErrorMap(error -> new SendMessageException(sourceContact.getPhone(), error.getMessage()))
                .doOnCancel(() -> handleSendMessageCancel(sourceContact));
    }

    private SendMessageRequest setSendRequest(String targetPhoneNumber, String templateName) {
        SendMessageRequest request = new SendMessageRequest();
        request.setMessaging_product(MESSAGING_PRODUCT);
        request.setTo(targetPhoneNumber);
        request.setType(MESSAGE_TYPE);
        request.setTemplate(setTemplate(templateName));
        return request;
    }

    private MessageTemplate setTemplate(String templateName) {
        MessageTemplate template = new MessageTemplate();
        template.setName(templateName);
        template.setLanguage(setLanguage());
        return template;
    }

    private MessageLanguage setLanguage() {
        MessageLanguage language = new MessageLanguage();
        language.setCode(languageCode);
        return language;
    }
}