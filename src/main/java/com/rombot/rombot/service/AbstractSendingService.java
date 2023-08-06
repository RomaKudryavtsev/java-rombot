package com.rombot.rombot.service;

import com.rombot.rombot.dto.ResultDto;
import com.rombot.rombot.entity.Result;
import com.rombot.rombot.entity.SourceContact;
import com.rombot.rombot.repo.ResultRepo;
import com.rombot.rombot.repo.SourceRepo;
import com.rombot.rombot.util.ResultMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Slf4j
public abstract class AbstractSendingService<T> implements ISendingService<T> {
    final static int BACKPRESSURE_BUFFER_SIZE = 50;
    final static int DEFAULT_PHONE_LENGTH = 12;
    final static String DEFAULT_PHONE_PREFIX = "972";
    @Autowired
    SourceRepo sourceRepo;
    @Autowired
    ResultRepo resultRepo;
    @Autowired
    WebClient sendingClient;
    boolean isSending;

    @Override
    public Flux<ResultDto> startSending(String templateName, Integer numberOfMessages, Integer delay) {
        return configureMainPipeline(numberOfMessages)
                .delayElements(Duration.ofSeconds(delay))
                .onBackpressureBuffer(BACKPRESSURE_BUFFER_SIZE)
                .doOnSubscribe(sourceContact -> this.isSending = true)
                .filter(sourceContact -> sourceContact.getPhone().length() == DEFAULT_PHONE_LENGTH &&
                        sourceContact.getPhone().startsWith(DEFAULT_PHONE_PREFIX))
                .flatMap(sourceContact -> resultRepo.existsByPhone(sourceContact.getPhone()).flatMap(b ->
                        b ? Mono.empty() : Mono.just(sourceContact)))
                .flatMap(sourceContact -> this.sendMessage(sourceContact, templateName)
                        .flatMap(resp -> this.saveResult(sourceContact)))
                .map(ResultMapper::entityToDto)
                .onErrorContinue((ex, errorResp) -> log.error(ex.getMessage()))
                .doOnCancel(this::handleMainPipelineCancel)
                .takeWhile(sourceContact -> isSending)
                .doOnComplete(() -> this.isSending = false);
    }

    @Override
    public Mono<Void> cancelSending() {
        if(!isSending) {
            log.error("Sending service is not running");
        } else {
            isSending = false;
            log.info("Cancelling messages");
        }
        return Mono.empty();
    }

    protected abstract Mono<T> sendMessage(SourceContact sourceContact, String templateName);

    private Flux<SourceContact> configureMainPipeline(Integer numberOfMessages) {
        return numberOfMessages == null ? sourceRepo.findAll() : sourceRepo.findAll().take(numberOfMessages);
    }

    private Mono<Result> saveResult(SourceContact sourceContact) {
        Result result = ResultMapper.sourceContactToResult(sourceContact);
        return resultRepo.save(result);
    }

    protected void handleSendMessageCancel(SourceContact sourceContact) {
        // TODO: implement logic to handle onCancel for the sendMessage method
        log.info("Message sending to contact canceled: " + sourceContact.toString());
    }

    private void handleMainPipelineCancel() {
        // TODO: implement logic to handle onCancel for the main pipeline
        log.info("Message sending process canceled.");
    }

}
