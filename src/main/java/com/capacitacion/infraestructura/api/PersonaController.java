package com.capacitacion.infraestructura.api;

import com.capacitacion.domain.model.exception.DniNoValidoException;
import com.capacitacion.domain.model.exception.NombreNoValidoException;
import com.capacitacion.domain.model.exception.PersonaNoExisteException;
import com.capacitacion.domain.model.exception.VidaErroneaException;
import com.capacitacion.domain.application.PersonaService;
import com.capacitacion.infraestructura.api.dto.PersonaDTO;
import com.capacitacion.domain.model.Persona;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/personas")
@Slf4j
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @GetMapping
    public ResponseEntity<List<Persona>> obtenerTodasLasPersonas() {
        List<Persona> personas = personaService.recuperarTodos();
        log.debug("Obteniendo todas las personas: {}", personas);
        return ResponseEntity.ok(personas);
    }

    @DeleteMapping
    public ResponseEntity<?> eliminarTodo() {
        personaService.eliminarTodo();
        log.warn("¡Atención! Se eliminaron todas las personas.");
        return ResponseEntity.ok().body("Las personas fueron eliminadas exitosamente");
    }

    @PostMapping
    public ResponseEntity<?> agregarPersona(@RequestBody PersonaDTO persona) {
        try {
            Persona personaCreada = personaService.crear(persona.aModelo());
            log.info("Persona creada con ID: {}", personaCreada.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(personaCreada);
        } catch (NombreNoValidoException | VidaErroneaException | DniNoValidoException e) {
            return handleBadRequest(e);
        }
        catch (Exception e) {
            return handleInternalServerError(e);
        }
    }

    @GetMapping(value ="/{id}", produces = "application/json")
    public ResponseEntity<?> obtenerPersonaPorId(@PathVariable String id) {
        try {
            Persona persona = personaService.recuperar(id);
            log.debug("Obteniendo persona por ID {}: {}", id, persona);
            return ResponseEntity.ok().body(persona);
        } catch (PersonaNoExisteException e) {
            return handleNotFound(e, id);
        } catch (Exception e) {
            return handleInternalServerError(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPersona(@PathVariable String id, @RequestBody PersonaDTO persona) {
        try {
            Persona personaActualizada = personaService.actualizar(id, persona.aModelo());
            log.info("Persona actualizada con ID: {}", id);
            return ResponseEntity.ok(personaActualizada);
        } catch (PersonaNoExisteException e) {
            return handleNotFound(e, id);
        } catch (NombreNoValidoException | VidaErroneaException  e) {
            return handleBadRequest(e);
        } catch (Exception e) {
            return handleInternalServerError(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPersona(@PathVariable String id) {
        try {
            personaService.eliminar(id);
            log.info("Persona eliminada con ID: {}", id);
            return ResponseEntity.ok().body("La persona fue eliminada exitosamente");
        } catch (PersonaNoExisteException e) {
            return handleNotFound(e, id);
        } catch (Exception e) {
            return handleInternalServerError(e);
        }
    }

    private ResponseEntity<?> handleBadRequest(RuntimeException e) {
        log.error("Error al procesar la solicitud", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    private ResponseEntity<?> handleNotFound(RuntimeException e, String id) {
        log.warn("No se encontró información para ID: {}", id, e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    private ResponseEntity<?> handleInternalServerError(Exception e) {
        log.error("Error interno del servidor al procesar solicitud", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
}