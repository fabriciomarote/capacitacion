package com.capacitacion.domain.repository;

import com.capacitacion.domain.model.Persona;
import org.springframework.data.repository.CrudRepository;

public interface PersonaRepository extends CrudRepository<Persona, Long> {

    Persona findPersonaById(Long id);
    Persona findByDni(String dni);
}