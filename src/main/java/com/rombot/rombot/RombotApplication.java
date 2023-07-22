package com.rombot.rombot;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.rombot.rombot")
@ComponentScan(basePackages = "com.rombot.rombot")
@OpenAPIDefinition(info = @Info(
        title = "rombot",
        version = "1.0",
        description = "Reactive WhatsApp message sending tool"))
public class RombotApplication {
    //To see Swagger UI use this link: http://localhost:8080/swagger-ui
    public static void main(String[] args) {
        SpringApplication.run(RombotApplication.class, args);
    }

}
