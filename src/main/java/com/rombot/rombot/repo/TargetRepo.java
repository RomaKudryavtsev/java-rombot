package com.rombot.rombot.repo;

import com.rombot.rombot.entity.Target;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetRepo extends MongoRepository<Target, Integer> {
}
