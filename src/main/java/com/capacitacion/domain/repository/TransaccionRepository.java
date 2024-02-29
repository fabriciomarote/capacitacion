package com.capacitacion.domain.repository;

import com.capacitacion.domain.model.Transaccion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaccionRepository extends MongoRepository<Transaccion, Long> {

}
