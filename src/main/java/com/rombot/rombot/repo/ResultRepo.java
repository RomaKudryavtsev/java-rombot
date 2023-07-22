package com.rombot.rombot.repo;

import com.rombot.rombot.entity.Result;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ResultRepo extends ReactiveMongoRepository<Result, Integer> {
    Mono<Boolean> existsByPhone(String phone);
}
