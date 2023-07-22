package com.rombot.rombot.service;

import com.rombot.rombot.dto.ResultDto;
import com.rombot.rombot.dto.wa_api_request.MessageLanguage;
import com.rombot.rombot.dto.wa_api_request.MessageTemplate;
import com.rombot.rombot.dto.wa_api_request.SendMessageRequest;
import com.rombot.rombot.dto.wa_api_response.SendMessageResponse;
import com.rombot.rombot.entity.Result;
import com.rombot.rombot.entity.SourceContact;
import com.rombot.rombot.exception.SendMessageException;
import com.rombot.rombot.repo.ResultRepo;
import com.rombot.rombot.repo.SourceRepo;
import com.rombot.rombot.util.ResultMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class SendingService {
    final static String MESSAGING_PRODUCT = "whatsapp";
    final static String MESSAGE_TYPE = "template";
    final static long EMISSION_DELAY_SEC = 2;
    final static int BACKPRESSURE_BUFFER_SIZE = 50;
    @Value("${rombot.lang.code}")
    String languageCode;
    @Value("${rombot.auth.token}")
    String token;
    final SourceRepo sourceRepo;
    final ResultRepo resultRepo;
    final WebClient sendingClient;

    public Flux<ResultDto> startSending(String templateName) {
        return sourceRepo.findAll()
                .flatMap(sourceContact -> resultRepo.existsByPhone(sourceContact.getPhone()).flatMap(b ->
                        b ? Mono.empty() : Mono.just(sourceContact)))
                .delayElements(Duration.ofSeconds(EMISSION_DELAY_SEC))
                .onBackpressureBuffer(BACKPRESSURE_BUFFER_SIZE)
                .flatMap(sourceContact -> this.sendMessage(sourceContact, templateName)
                        .flatMap(resp -> this.saveResult(sourceContact))
                        .onErrorMap(error -> new SendMessageException(sourceContact.getPhone(), error.getMessage()))
                        .doOnCancel(() -> handleSendMessageCancel(sourceContact)))
                .map(ResultMapper::entityToDto)
                .onErrorContinue((ex, errorResp) -> log.error(ex.getMessage()))
                .doOnCancel(this::handleMainPipelineCancel);
    }

    private Mono<SendMessageResponse> sendMessage(SourceContact sourceContact, String templateName) {
        return sendingClient
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .bodyValue(setSendRequest(sourceContact.getPhone(), templateName))
                .retrieve().bodyToMono(SendMessageResponse.class);
    }

    private Mono<Result> saveResult(SourceContact sourceContact) {
        Result result = new Result();
        result.setPhone(sourceContact.getPhone());
        result.setName(sourceContact.getName());
        result.setSent(LocalDateTime.now());
        return resultRepo.save(result);
    }

    private void handleSendMessageCancel(SourceContact sourceContact) {
        // TODO: implement logic to handle onCancel for the sendMessage method
        log.info("Message sending to contact canceled: " + sourceContact.toString());
    }

    private void handleMainPipelineCancel() {
        // TODO: implement logic to handle onCancel for the main pipeline
        log.info("Message sending process canceled.");
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