package com.capacitacion.infraestructura.api;

import com.capacitacion.domain.model.Transaccion;
import com.capacitacion.domain.application.TransaccionService;
import com.capacitacion.domain.model.exception.*;
import com.capacitacion.infraestructura.api.dto.TransaccionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaccion")
@Slf4j
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;

    /**
     * Realiza una transacción a partir de los datos proporcionados en el cuerpo de la solicitud.
     *
     * @param transaccion Datos de la transacción.
     * @return Respuesta HTTP con la transacción realizada.
     */
    @PostMapping
    public ResponseEntity<?> realizarTransaccion(@RequestBody TransaccionDTO transaccion) {
        try {
            Transaccion transaccionNueva = transaccionService.realizarTransaccion(transaccion.aModelo());
            log.info("Transaccion realizada con ID: {}", transaccionNueva.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(transaccionNueva);
        } catch (DniNoValidoException | MontoNoValidoException | PersonaNoExisteException |
                 DniRepetidoException | MontoInsuficienteException e) {
            return handleBadRequest(e);
        } catch (Exception e) {
            return handleInternalServerError(e);
        }
    }

    /**
     * Elimina todas las transacciones.
     *
     * @return Respuesta HTTP indicando el éxito de la operación.
     */
    @DeleteMapping
    public ResponseEntity<?> eliminarTodo() {
        transaccionService.eliminarTodo();
        log.warn("¡Atención! Se eliminaron todas las transacciones.");
        return ResponseEntity.ok().body("Las transacciones fueron eliminadas exitosamente");
    }

    /**
     * Maneja las respuestas de error cuando ocurre una excepción de tipo {@code RuntimeException}.
     *
     * @param e Excepción capturada.
     * @return Respuesta HTTP con el código de estado y el mensaje de error correspondientes.
     */
    private ResponseEntity<?> handleBadRequest(RuntimeException e) {
        log.error("Error al realizar la transacción", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * Maneja las respuestas de error cuando ocurre una excepción de tipo {@code Exception}.
     *
     * @param e Excepción capturada.
     * @return Respuesta HTTP con el código de estado y un mensaje de error genérico.
     */
    private ResponseEntity<?> handleInternalServerError(Exception e) {
        log.error("Error al realizar la transacción", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
}
