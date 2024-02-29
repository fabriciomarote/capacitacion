package com.capacitacion.domain.application.impl;

import com.capacitacion.domain.model.PersonaEvent;
import com.capacitacion.domain.model.exception.*;
import com.capacitacion.domain.repository.PersonaRepository;
import com.capacitacion.domain.application.PersonaService;
import com.capacitacion.domain.model.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PersonaServiceImpl implements PersonaService {

    @Autowired
    private final PersonaRepository personaRepository;
    @Autowired
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Constructor de PersonaServiceImpl.
     *
     * @param personaRepository Repositorio de Persona para acceder a la base de datos.
     * @param kafkaTemplate     Plantilla de Kafka para enviar eventos relacionados con la entidad Persona.
     */
    public PersonaServiceImpl(PersonaRepository personaRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.personaRepository = personaRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Crea una nueva persona y la guarda en la base de datos.
     *
     * @param persona Persona a ser creada y almacenada.
     * @return Persona creada y almacenada.
     * @throws NombreNoValidoException Si el nombre de la persona es nulo o vacío.
     * @throws VidaErroneaException    Si la edad de la persona está fuera del rango permitido.
     * @throws DniNoValidoException    Si el DNI de la persona es nulo o no tiene la longitud adecuada.
     * @throws DniAsignadoException    Si el DNI ya está asignado a otra persona.
     */
    @Override
    public Persona crear(Persona persona) {
        validar(persona);
        Persona personaNueva = personaRepository.save(persona);

        PersonaEvent personaEvent = new PersonaEvent(personaNueva.getNombre());
        kafkaTemplate.send("alta-persona-topic", personaEvent);

        return personaNueva;
    }

    /**
     * Realiza la validación general de los datos de una persona antes de crearla.
     *
     * @param persona Persona a ser validada.
     * @throws NombreNoValidoException Si el nombre de la persona es nulo o vacío.
     * @throws VidaErroneaException    Si la edad de la persona está fuera del rango permitido.
     * @throws DniNoValidoException    Si el DNI de la persona es nulo o no tiene la longitud adecuada.
     * @throws DniAsignadoException    Si el DNI ya está asignado a otra persona.
     */
    private void validar(Persona persona) {
        validarNombre(persona.getNombre());
        validarEdad(persona.getEdad());
        validarDni(persona.getDni());
    }

    /**
     * Valida que el nombre de la persona no sea nulo ni vacío.
     *
     * @param nombrePersona Nombre de la persona a ser validado.
     * @throws NombreNoValidoException Si el nombre de la persona es nulo o vacío.
     */
    private void validarNombre(String nombrePersona)  {
        if(nombrePersona == null || nombrePersona.trim().isEmpty()) {
            throw new NombreNoValidoException();
        }
    }

    /**
     * Valida que la edad de la persona esté dentro del rango permitido.
     *
     * @param edadPersona Edad de la persona a ser validada.
     * @throws RuntimeException      Si la edad es nula.
     * @throws VidaErroneaException    Si la edad de la persona está fuera del rango permitido.
     */
    private void validarEdad(Integer edadPersona) {
        if (edadPersona == null) {
            throw new RuntimeException("Message Error: La edad no puede ser vacia y tiene que ser de tipo Number");
        }
        if(edadPersona < 1 || edadPersona >= 100) {
            throw new VidaErroneaException();
        }
    }

    /**
     * Valida que el DNI de la persona cumpla con los criterios establecidos.
     *
     * @param dniPersona DNI de la persona a ser validado.
     * @throws DniNoValidoException    Si el DNI de la persona es nulo o no tiene la longitud adecuada.
     * @throws DniAsignadoException    Si el DNI ya está asignado a otra persona.
     */
    private void validarDni(String dniPersona)  {
        Persona persona = personaRepository.findByDni(dniPersona);
        if (dniPersona == null || dniPersona.length() != 8) {
            throw new DniNoValidoException();
        }
        if (persona != null) {
            throw new DniAsignadoException();
        }
    }

    /**
     * Actualiza los datos de una persona existente en la base de datos.
     *
     * @param idPersona          Identificador único de la persona a ser actualizada.
     * @param personaActualizada Nuevos datos de la persona.
     * @return Persona actualizada.
     * @throws PersonaNoExisteException Si no se encuentra una persona con el ID proporcionado.
     */
    @Override
    public Persona actualizar(String idPersona, Persona personaActualizada) {
        Persona persona = this.recuperar(idPersona);
        persona.setEdad(personaActualizada.getEdad());
        persona.setNombre(personaActualizada.getNombre());
        persona.setCreditos(personaActualizada.getCreditos());
        persona.setDireccion(personaActualizada.getDireccion());
        return personaRepository.save(persona);
    }

    /**
     * Recupera una persona por su ID.
     *
     * @param idPersona Identificador único de la persona a recuperar.
     * @return Persona recuperada.
     * @throws PersonaNoExisteException Si no se encuentra una persona con el ID proporcionado.
     */
    @Override
    public Persona recuperar(String idPersona) {
        Persona persona = personaRepository.findPersonaById(idPersona);
        if(persona == null) {
            throw new PersonaNoExisteException();
        }
        return persona;
    }

    /**
     * Recupera una persona por su DNI.
     *
     * @param dniPersona DNI de la persona a recuperar.
     * @return Persona recuperada.
     * @throws PersonaNoExisteException Si no se encuentra una persona con el DNI proporcionado.
     */
    @Override
    public Persona obtenerPorDni(String dniPersona) {
        Persona persona = personaRepository.findByDni(dniPersona); //TODO: agregar validacion para dni repetido
        if(persona == null) {
            throw new PersonaNoExisteException();
        }
        return persona;
    }

    /**
     * Recupera una persona por su nombre.
     *
     * @param nombrePersona Nombre de la persona a recuperar.
     * @return Persona recuperada.
     * @throws PersonaNoExisteException Si no se encuentra una persona con el nombre proporcionado.
     */
    @Override
    public Persona obtenerPorNombre(String nombrePersona) {
        Persona persona = personaRepository.findByNombre(nombrePersona); //TODO: agregar validacion para nombre repetido
        if(persona == null) {
            throw new PersonaNoExisteException();
        }
        return persona;
    }

    /**
     * Elimina una persona de la base de datos por su ID.
     *
     * @param idPersona Identificador único de la persona a ser eliminada.
     * @throws PersonaNoExisteException Si no se encuentra una persona con el ID proporcionado.
     */
    @Override
    public void eliminar(String idPersona) {
        Persona persona = this.recuperar(idPersona);
        personaRepository.delete(persona);
    }

    /**
     * Recupera todas las personas almacenadas en la base de datos.
     *
     * @return Lista de todas las personas.
     */
    @Override
    public List<Persona> recuperarTodos() {
        return personaRepository.findAll();
    }

    /**
     * Elimina todas las personas de la base de datos.
     */
    @Override
    public void eliminarTodo() {
        personaRepository.deleteAll();
    }
}
