package com.capacitacion.application.impl;

import com.capacitacion.domain.model.PersonaMongo;
import com.capacitacion.domain.model.exception.DniNoValidoException;
import com.capacitacion.domain.model.exception.PersonaNoExisteException;
import com.capacitacion.domain.repository.PersonaRepository;
import com.capacitacion.application.PersonaMongoService;
import com.capacitacion.application.PersonaService;
import com.capacitacion.domain.model.Persona;
import com.capacitacion.domain.model.exception.NombreNoValidoException;
import com.capacitacion.domain.model.exception.VidaErroneaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaServiceImpl implements PersonaService {

    @Autowired
    private final PersonaRepository personaRepository;
    @Autowired
    private final PersonaMongoService personaMongoService;

    public PersonaServiceImpl(PersonaRepository personaRepository, PersonaMongoService personaMongoService) {
        this.personaRepository = personaRepository;
        this.personaMongoService = personaMongoService;
    }

    @Override
    public Persona crear(Persona persona) {
        validar(persona);
        Persona personaNueva = personaRepository.save(persona);
        PersonaMongo personaMongo = new PersonaMongo(personaNueva.getId(), personaNueva.getNombre(), personaNueva.getEdad(), personaNueva.getDni());
        personaMongoService.crear(personaMongo);
        return personaNueva;
    }

    private void validar(Persona persona) {
        validarNombre(persona.getNombre());
        validarEdad(persona.getEdad());
        validarDni(persona.getDni());
    }

    private void validarNombre(String nombrePersona)  {
        if(nombrePersona == null || nombrePersona.trim().isEmpty()) {
            throw new NombreNoValidoException();
        }
    }

    private void validarEdad(Integer edadPersona) {
        if (edadPersona == null) {
            throw new RuntimeException("Message Error: La edad no puede ser vacia y tiene que ser de tipo Number");
        }
        if(edadPersona < 1 || edadPersona >= 100) {
            throw new VidaErroneaException();
        }
    }

    private void validarDni(String dniPersona)  {
        if(dniPersona == null || dniPersona.length() != 8) {
            throw new DniNoValidoException();
        }
    }

    @Override
    public Persona actualizar(Long idPersona, Persona personaActualizada) {
            Persona persona = this.recuperar(idPersona);
            persona.setEdad(personaActualizada.getEdad());
            persona.setNombre(personaActualizada.getNombre());
            return this.crear(persona);
    }

    @Override
    public Persona recuperar(Long idPersona) {
        Persona persona = personaRepository.findPersonaById(idPersona);
        if(persona == null) {
            throw new PersonaNoExisteException();
        }
        return persona;
    }

    @Override
    public Persona obtenerPorDni(String dniPersona) {
        Persona persona = personaRepository.findByDni(dniPersona);
        if(persona == null) {
            throw new PersonaNoExisteException();
        }
        return persona;
    }

    @Override
    public void eliminar(Long idPersona) {
        Persona persona = this.recuperar(idPersona);
        personaRepository.delete(persona);
        personaMongoService.eliminar(persona.getId());
    }

    @Override
    public List<Persona> recuperarTodos() {
        return (List<Persona>) personaRepository.findAll();
    }

    @Override
    public void realizarTransaccion(String dni1, String dni2, int monto) {
        Persona personaOrigen = personaRepository.findByDni(dni1);
        Persona personaDestino = personaRepository.findByDni(dni2);
        PersonaMongo personaMongoOrigen = personaMongoService.recuperar(personaOrigen.getId());
        PersonaMongo personaMongoDestino = personaMongoService.recuperar(personaDestino.getId());
        personaOrigen.restarCreditoPorTransaccion(monto);
        personaDestino.aumentarCreditoPorTransaccion(monto);
        personaMongoService.actualizar(personaMongoOrigen);
        personaMongoService.actualizar(personaMongoDestino);
    }
}
