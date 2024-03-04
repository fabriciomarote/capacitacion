package com.capacitacion.infraestructura.api;

import com.capacitacion.domain.model.Transaccion;
import com.capacitacion.domain.application.TransaccionService;
import com.capacitacion.domain.model.exception.*;
import com.capacitacion.infraestructura.api.dto.TransaccionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Realiza una transacción",
            description = "Este endpoint permite realizar una transacción a partir de los datos proporcionados en el cuerpo de la solicitud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transacción realizada exitosamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Transaccion.class))}),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta o datos no válidos",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> realizarTransaccion(@RequestBody TransaccionDTO transaccion) {
        try {
            Transaccion transaccionNueva = transaccionService.realizarTransaccion(transaccion.aModelo());
            log.info("Transaccion realizada con ID: {}", transaccionNueva.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(transaccionNueva);
        } catch (DniNoValidoException e) {
            return handleBadRequest("DNI no válido: " + e.getMessage());
        } catch (MontoNoValidoException e) {
            return handleBadRequest("Monto no válido: " + e.getMessage());
        } catch (PersonaNoExisteException e) {
            return handleBadRequest("Persona inexistente: " + e.getMessage());
        } catch (MontoInsuficienteException e) {
            return handleBadRequest("Monto Insuficiente: " + e.getMessage());
        } catch (DniRepetidoException e) {
            return handleBadRequest("DNI repetido: " + e.getMessage());
        } catch (Exception e) {
            return handleInternalServerError(e);
        }
    }

    /**
     * Elimina todas las transacciones.
     *
     * @return Respuesta HTTP indicando el éxito de la operación.
     */
    @Operation(summary = "Elimina todas las transacciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Las transacciones fueron eliminadas exitosamente",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)})
    @DeleteMapping
    public ResponseEntity<?> eliminarTodo() {
        try {
            transaccionService.eliminarTodo();
            log.warn("¡Atención! Se eliminaron todas las transacciones.");
            return ResponseEntity.ok().body("Las transacciones fueron eliminadas exitosamente");
        } catch (Exception e) {
            return handleInternalServerError(e);
        }
    }

    /**
     * Maneja los errores de solicitud incorrecta (HTTP 400 Bad Request).
     *
     * @param errorMessage Mensaje de error detallado.
     * @return ResponseEntity con el mensaje de error y el estado HTTP 400 (Bad Request).
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<?> handleBadRequest(String errorMessage) {
        log.error("Error al procesar la solicitud: {}", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    /**
     * Maneja las respuestas de error cuando ocurre una excepción de tipo {@code Exception}.
     *
     * @param e Excepción capturada.
     * @return Respuesta HTTP con el código de estado y un mensaje de error genérico.
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<?> handleInternalServerError(Exception e) {
        log.error("Error al realizar la transacción", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
}
