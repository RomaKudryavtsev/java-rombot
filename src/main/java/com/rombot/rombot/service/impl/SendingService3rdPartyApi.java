package com.rombot.rombot.service.impl;

import com.rombot.rombot.dto.ultramsg_request.UltraMsgRequest;
import com.rombot.rombot.dto.ultramsg_response.UltraMsgResponse;
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

@Service
@Primary
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class SendingService3rdPartyApi extends AbstractSendingService<UltraMsgResponse> {
    @Value("${rombot.ultramsg.token}")
    String token;

    //Sample 3rd party API
    //TODO: add check whether message was actually sent
    @Override
    protected Mono<UltraMsgResponse> sendMessage(SourceContact sourceContact, String template) {
        return sendingClient
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(setSendRequest(sourceContact.getPhone(), template))
                .retrieve().bodyToMono(UltraMsgResponse.class)
                .doOnNext(sendMessageResponse -> log.info("SENT: {}", sourceContact.getPhone()))
                .onErrorMap(error -> new SendMessageException(sourceContact.getPhone(), error.getMessage()))
                .doOnCancel(() -> handleSendMessageCancel(sourceContact));
    }

    private UltraMsgRequest setSendRequest(String phone, String template) {
        return new UltraMsgRequest(token, phone, template);
    }
}
