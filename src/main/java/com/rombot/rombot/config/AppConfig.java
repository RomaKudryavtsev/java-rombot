package com.rombot.rombot.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppConfig {
    @Value("${rombot.phone.id}")
    String phoneId;

    @Bean
    public WebClient getWebClient() {
        String baseUrl = UriComponentsBuilder
                .fromUriString("https://graph.facebook.com/v17.0/{phoneId}/messages")
                .buildAndExpand(phoneId)
                .toUriString();
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
