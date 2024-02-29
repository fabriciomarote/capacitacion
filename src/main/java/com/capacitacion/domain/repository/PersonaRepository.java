package com.capacitacion.domain.repository;

import com.capacitacion.domain.model.Persona;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends MongoRepository<Persona, String> {

    @Query("{id: ?0}")
    Persona findPersonaById(String id);
    Persona findByDni(String dni);
    Persona findByNombre(String nombre);
}