package com.capacitacion.domain.application.impl;
import com.capacitacion.domain.model.Persona;
import com.capacitacion.domain.model.TransaccionEvent;
import com.capacitacion.domain.model.exception.*;
import com.capacitacion.domain.repository.TransaccionRepository;
import com.capacitacion.domain.model.Transaccion;
import com.capacitacion.domain.application.PersonaService;
import com.capacitacion.domain.application.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    @Autowired
    private final TransaccionRepository transaccionRepository;
    @Autowired
    private final PersonaService personaService;
    @Autowired
    private final KafkaTemplate<String, TransaccionEvent> kafkaTemplate;

    @Autowired
    public TransaccionServiceImpl(TransaccionRepository transaccionRepository, PersonaService personaService, KafkaTemplate<String, TransaccionEvent> kafkaTemplate) {
        this.transaccionRepository = transaccionRepository;
        this.personaService = personaService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Transaccion realizarTransaccion(Transaccion transaccion) {
        validarCampos(transaccion);
        validarTransaccion(transaccion);
        actualizarPartes(transaccion.getDniOrigen(), transaccion.getDniDestino(), transaccion.getMonto());
        Transaccion nuevaTransaccion = transaccionRepository.save(transaccion);

        TransaccionEvent transaccionEvent = new TransaccionEvent(nuevaTransaccion.getId());
        kafkaTemplate.send("transaccion-topic", transaccionEvent);
        return nuevaTransaccion;
    }

    @Override
    public void eliminarTodo() {
        transaccionRepository.deleteAll();
    }

    private void validarCampos(Transaccion transaccion) {
        validarDni(transaccion.getDniOrigen());
        validarDni(transaccion.getDniDestino());
        validarMonto(transaccion.getMonto());
    }

    private void validarDni(String dniPersona)  {
        if(dniPersona == null || dniPersona.length() != 8) {
            throw new DniNoValidoException();
        }
    }

    private void validarMonto(int monto) {
        if (monto <= 0 ) {
            throw new MontoNoValidoException();
        }
    }

    private void validarTransaccion(Transaccion transaccion) {
        validarPersona(transaccion.getDniOrigen());
        validarPersona(transaccion.getDniDestino());
        verificarDnis(transaccion.getDniOrigen(), transaccion.getDniDestino());
        validarMontoATransferir(transaccion.getDniOrigen(), transaccion.getMonto());
    }

    private void validarPersona(String dniPersona) {
        Persona persona = personaService.obtenerPorDni(dniPersona);
        if (persona == null) {
            throw new PersonaNoExisteException();
        }
    }

    private void verificarDnis(String dniPersona1, String dniPersona2) {
        if (Objects.equals(dniPersona1, dniPersona2)) {
            throw new DniRepetidoException();
        }
    }

    private void validarMontoATransferir(String dniPersona, int monto) {
        Persona persona = personaService.obtenerPorDni(dniPersona);
        if (persona.getCreditos() < monto) {
            throw new MontoInsuficienteException();
        }
    }

    private void actualizarPartes(String dniOrigen, String dniDestino, int monto) {
        Persona personaOrigen = personaService.obtenerPorDni(dniOrigen);
        Persona personaDestino = personaService.obtenerPorDni(dniDestino);
        personaOrigen.restarCreditoPorTransaccion(monto);
        personaDestino.aumentarCreditoPorTransaccion(monto);
        personaService.actualizar(personaOrigen.getId(), personaOrigen);
        personaService.actualizar(personaDestino.getId(), personaDestino);
    }
}
