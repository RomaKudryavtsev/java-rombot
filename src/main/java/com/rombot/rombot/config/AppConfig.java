package com.rombot.rombot.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppConfig {
    //Official WhatsApp Business API
    @Value("${rombot.phone.id}")
    String phoneId;
    //Sample 3rd party API
    @Value("${rombot.ultramsg.instance.id}")
    String instanceId;

    //Official WhatsApp Business API
    @Bean
    public WebClient getWebClientOfficial() {
        String baseUrl = UriComponentsBuilder
                .fromUriString("https://graph.facebook.com/v17.0/{phoneId}/messages")
                .buildAndExpand(phoneId)
                .toUriString();
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    //Sample 3rd party API
    @Bean
    @Primary
    public WebClient getWebClient3rdParty() {
        String baseUrl = UriComponentsBuilder
                .fromUriString("https://api.ultramsg.com/{instanceId}/messages/chat")
                .buildAndExpand(instanceId)
                .toUriString();
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
