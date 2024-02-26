package com.capacitacion.domain.repository;

import com.capacitacion.domain.model.PersonaMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PersonaMongoRepository extends MongoRepository<PersonaMongo, String> {
    @Query("{id: ?0}")
    public PersonaMongo findByIdNoSQL(Long id);
}
