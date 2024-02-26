package com.capacitacion.domain.repository;

import com.capacitacion.domain.model.TransaccionMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransaccionMongoRepository extends MongoRepository<TransaccionMongo, String> {
}
