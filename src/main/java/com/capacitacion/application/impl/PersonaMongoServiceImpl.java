package com.capacitacion.application.impl;

import com.capacitacion.domain.repository.PersonaMongoRepository;
import com.capacitacion.application.PersonaMongoService;
import com.capacitacion.domain.model.PersonaMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class PersonaMongoServiceImpl implements PersonaMongoService {

    @Autowired
    private final PersonaMongoRepository personaMongoRepository;

    public PersonaMongoServiceImpl(PersonaMongoRepository personaMongoRepository) {
        this.personaMongoRepository = personaMongoRepository;
    }

    @Override
    public PersonaMongo crear(PersonaMongo persona) {
        return personaMongoRepository.save(persona);
    }

    @Override
    public PersonaMongo actualizar(PersonaMongo persona) {
        return this.crear(persona);
    }

    @Override
    public PersonaMongo recuperar(Long idPersona) {
        try {
            PersonaMongo persona = personaMongoRepository.findByIdNoSQL(idPersona);
            return persona;
        } catch (RuntimeException e) {
           throw new RuntimeException("El id no existe");
        }
    }

    @Override
    public void eliminar(Long idPersona) {
        PersonaMongo persona = this.recuperar(idPersona);
        personaMongoRepository.delete(persona);
    }

    @Override
    public List<PersonaMongo> recuperarTodos() {
        return personaMongoRepository.findAll();
    }
}
