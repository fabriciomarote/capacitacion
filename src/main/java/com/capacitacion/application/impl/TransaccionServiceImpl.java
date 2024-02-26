package com.capacitacion.application.impl;
import com.capacitacion.application.TransaccionMongoService;
import com.capacitacion.domain.model.Persona;
import com.capacitacion.domain.model.TransaccionMongo;
import com.capacitacion.domain.model.exception.*;
import com.capacitacion.domain.repository.TransaccionRepository;
import com.capacitacion.domain.model.Transaccion;
import com.capacitacion.application.PersonaService;
import com.capacitacion.application.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TransaccionServiceImpl implements TransaccionService {
    @Autowired
    private final TransaccionRepository transaccionRepository;
    @Autowired
    private final PersonaService personaService;
    @Autowired
    private final TransaccionMongoService transaccionMongoService;

    public TransaccionServiceImpl(TransaccionRepository transaccionRepository, PersonaService personaService, TransaccionMongoService transaccionMongoService) {
        this.transaccionRepository = transaccionRepository;
        this.personaService = personaService;
        this.transaccionMongoService = transaccionMongoService;
    }

    @Override
    public Transaccion realizarTransaccion(Transaccion transaccion) {
        System.out.println(transaccion.getDniOrigen());
        System.out.println(transaccion.getDniDestino());
        System.out.println(transaccion.getMonto());
        validarCampos(transaccion);
        validarTransaccion(transaccion);
        personaService.realizarTransaccion(transaccion.getDniOrigen(), transaccion.getDniDestino(), transaccion.getMonto());
        Transaccion nuevaTransaccion = transaccionRepository.save(transaccion);
        TransaccionMongo transaccionMongo = new TransaccionMongo(nuevaTransaccion.getId(), nuevaTransaccion.getDniOrigen(), nuevaTransaccion.getDniDestino(), nuevaTransaccion.getMonto());
        transaccionMongoService.crear(transaccionMongo);
        return nuevaTransaccion;
    }

    public void validarCampos(Transaccion transaccion) {
        validarDni(transaccion.getDniOrigen());
        validarDni(transaccion.getDniDestino());
        validarMonto(transaccion.getMonto());
    }

    private void validarDni(String dniPersona)  {
        System.out.println(dniPersona);
        if(dniPersona == null || dniPersona.length() != 8) {
            throw new DniNoValidoException();
        }
    }

    public void validarMonto(int monto) {
        if (monto <= 0 ) {
            throw new MontoNoValidoException();
        }
    }

    public void validarTransaccion(Transaccion transaccion) {
        validarPersona(transaccion.getDniOrigen());
        validarPersona(transaccion.getDniDestino());
        verificarDnis(transaccion.getDniOrigen(), transaccion.getDniDestino());
        validarMontoATransferir(transaccion.getDniOrigen(), transaccion.getMonto());
    }

    public void validarPersona(String dniPersona) {
        Persona persona = personaService.obtenerPorDni(dniPersona);
        if (persona == null) {
            throw new PersonaNoExisteException();
        }
    }

    public void verificarDnis(String dniPersona1, String dniPersona2) {
        if (Objects.equals(dniPersona1, dniPersona2)) {
            throw new DniRepetidoException();
        }
    }

    public void validarMontoATransferir(String dniPersona, int monto) {
        Persona persona = personaService.obtenerPorDni(dniPersona);
        if (persona.getCreditos() < monto) {
            throw new MontoInsuficienteException();
        }
    }
}
