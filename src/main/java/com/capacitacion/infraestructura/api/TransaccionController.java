package com.capacitacion.infraestructura.api;

import com.capacitacion.domain.model.Transaccion;
import com.capacitacion.domain.application.TransaccionService;
import com.capacitacion.domain.model.exception.*;
import com.capacitacion.infraestructura.api.dto.TransaccionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/transaccion")
public class TransaccionController {
    private static final Logger logger = LoggerFactory.getLogger(PersonaController.class);

    @Autowired
    private TransaccionService transaccionService;

    @PostMapping
    public ResponseEntity<?> realizarTransaccion(@RequestBody TransaccionDTO transaccion) {
        try {
            Transaccion transaccionNueva = transaccionService.realizarTransaccion(transaccion.aModelo());
            logger.info("Transaccion realizada con ID: {}", transaccionNueva.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(transaccionNueva);
        } catch (DniNoValidoException | MontoNoValidoException | PersonaNoExisteException |
                 DniRepetidoException | MontoInsuficienteException e) {
            logger.error("Error al realizar la transaccion con ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al realizar la transaccion", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> eliminarTodo() {
        transaccionService.eliminarTodo();
        return ResponseEntity.ok().body("Las transacciones fueron eliminadas exitosamente");
    }
}
