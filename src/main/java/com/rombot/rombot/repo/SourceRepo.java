package com.rombot.rombot.repo;

import com.rombot.rombot.entity.SourceContact;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceRepo extends ReactiveMongoRepository<SourceContact, Integer> {
}
