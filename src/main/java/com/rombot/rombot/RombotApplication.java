package com.rombot.rombot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.rombot.rombot")
public class RombotApplication {
	public static void main(String[] args) {
		SpringApplication.run(RombotApplication.class, args);
	}

}
