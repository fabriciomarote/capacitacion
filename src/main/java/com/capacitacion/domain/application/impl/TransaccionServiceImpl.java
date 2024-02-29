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
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Constructor de TransaccionServiceImpl.
     *
     * @param transaccionRepository Repositorio de Transacciones.
     * @param personaService        Servicio de Persona.
     * @param kafkaTemplate         Plantilla de Kafka.
     */
    @Autowired
    public TransaccionServiceImpl(TransaccionRepository transaccionRepository, PersonaService personaService, KafkaTemplate<String, Object> kafkaTemplate) {
        this.transaccionRepository = transaccionRepository;
        this.personaService = personaService;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Realiza una transacción, valida campos, realiza actualizaciones y envía un evento a Kafka.
     *
     * @param transaccion Transacción a realizar.
     * @return La nueva Transacción creada.
     * @throws DniNoValidoException        Si el DNI no tiene el formato adecuado.
     * @throws MontoNoValidoException      Si el monto de la transacción no es válido.
     * @throws PersonaNoExisteException    Si una de las personas involucradas en la transacción no existe.
     * @throws DniRepetidoException        Si los DNIs de origen y destino son iguales.
     * @throws MontoInsuficienteException  Si el monto a transferir excede los créditos disponibles en la cuenta de origen.
     */
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

    /**
     * Elimina todas las transacciones.
     */
    @Override
    public void eliminarTodo() {
        transaccionRepository.deleteAll();
    }

    /**
     * Valida los campos de una transacción.
     *
     * @param transaccion Transacción a validar.
     * @throws DniNoValidoException   Si el DNI no tiene el formato adecuado.
     * @throws MontoNoValidoException Si el monto de la transacción no es válido.
     */
    private void validarCampos(Transaccion transaccion) {
        validarDni(transaccion.getDniOrigen());
        validarDni(transaccion.getDniDestino());
        validarMonto(transaccion.getMonto());
    }

    /**
     * Valida el formato del DNI.
     *
     * @param dniPersona DNI a validar.
     * @throws DniNoValidoException Si el DNI no tiene el formato adecuado.
     */
    private void validarDni(String dniPersona)  {
        if(dniPersona == null || dniPersona.length() != 8) {
            throw new DniNoValidoException();
        }
    }

    /**
     * Valida que el monto de la transacción sea válido.
     *
     * @param monto Monto a validar.
     * @throws MontoNoValidoException Si el monto de la transacción no es válido.
     */
    private void validarMonto(int monto) {
        if (monto <= 0 ) {
            throw new MontoNoValidoException();
        }
    }

    /**
     * Valida la transacción, verificando la existencia de las personas, la no repetición de DNIs y la suficiencia de créditos.
     *
     * @param transaccion Transacción a validar.
     * @throws PersonaNoExisteException   Si una de las personas involucradas en la transacción no existe.
     * @throws DniRepetidoException       Si los DNIs de origen y destino son iguales.
     * @throws MontoInsuficienteException Si el monto a transferir excede los créditos disponibles en la cuenta de origen.
     */
    private void validarTransaccion(Transaccion transaccion) {
        validarPersona(transaccion.getDniOrigen());
        validarPersona(transaccion.getDniDestino());
        verificarDnis(transaccion.getDniOrigen(), transaccion.getDniDestino());
        validarMontoATransferir(transaccion.getDniOrigen(), transaccion.getMonto());
    }

    /**
     * Valida la existencia de una persona por su DNI.
     *
     * @param dniPersona DNI de la persona a validar.
     * @throws PersonaNoExisteException Si la persona con el DNI especificado no existe.
     */
    private void validarPersona(String dniPersona) {
        Persona persona = personaService.obtenerPorDni(dniPersona);
        if (persona == null) {
            throw new PersonaNoExisteException();
        }
    }

    /**
     * Verifica que los DNIs no sean iguales en una transacción.
     *
     * @param dniPersona1 Primer DNI a comparar.
     * @param dniPersona2 Segundo DNI a comparar.
     * @throws DniRepetidoException Si los DNIs de origen y destino son iguales.
     */
    private void verificarDnis(String dniPersona1, String dniPersona2) {
        if (Objects.equals(dniPersona1, dniPersona2)) {
            throw new DniRepetidoException();
        }
    }

    /**
     * Valida que el monto a transferir no exceda los créditos disponibles en la cuenta de origen.
     *
     * @param dniPersona DNI de la persona origen.
     * @param monto Monto a transferir.
     * @throws MontoInsuficienteException Si el monto a transferir excede los créditos disponibles en la cuenta de origen.
     */
    private void validarMontoATransferir(String dniPersona, int monto) {
        Persona persona = personaService.obtenerPorDni(dniPersona);
        if (persona.getCreditos() < monto) {
            throw new MontoInsuficienteException();
        }
    }

    /**
     * Actualiza las cuentas de origen y destino después de una transacción.
     *
     * @param dniOrigen DNI de la persona de origen.
     * @param dniDestino DNI de la persona de destino.
     * @param monto Monto de la transacción.
     */
    private void actualizarPartes(String dniOrigen, String dniDestino, int monto) {
        Persona personaOrigen = personaService.obtenerPorDni(dniOrigen);
        Persona personaDestino = personaService.obtenerPorDni(dniDestino);
        personaOrigen.restarCreditoPorTransaccion(monto);
        personaDestino.aumentarCreditoPorTransaccion(monto);
        personaService.actualizar(personaOrigen.getId(), personaOrigen);
        personaService.actualizar(personaDestino.getId(), personaDestino);
    }
}
