package com.bingo.round;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundRepository extends ReactiveMongoRepository<Round, String> {
}
