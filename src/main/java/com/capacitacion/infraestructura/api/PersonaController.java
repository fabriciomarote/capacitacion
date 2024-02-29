package com.capacitacion.infraestructura.api;

import com.capacitacion.domain.model.exception.*;
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

    /**
     * Obtiene todas las personas almacenadas en la base de datos.
     *
     * @return ResponseEntity con la lista de personas y el estado HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<Persona>> obtenerTodasLasPersonas() {
        List<Persona> personas = personaService.recuperarTodos();
        log.debug("Obteniendo todas las personas: {}", personas);
        return ResponseEntity.ok(personas);
    }

    /**
     * Elimina todas las personas de la base de datos.
     *
     * @return ResponseEntity con el mensaje de éxito y el estado HTTP 200 (OK).
     */
    @DeleteMapping
    public ResponseEntity<?> eliminarTodo() {
        personaService.eliminarTodo();
        log.warn("¡Atención! Se eliminaron todas las personas.");
        return ResponseEntity.ok().body("Las personas fueron eliminadas exitosamente");
    }

    /**
     * Agrega una nueva persona a la base de datos.
     *
     * @param persona PersonaDTO que representa los datos de la persona a ser creada.
     * @return ResponseEntity con la persona creada y el estado HTTP 201 (CREATED).
     *         En caso de errores de validación, retorna un ResponseEntity con el mensaje de error y el estado HTTP 400 (BAD REQUEST).
     *         En caso de otros errores, retorna un ResponseEntity con un mensaje de error genérico y el estado HTTP 500 (INTERNAL SERVER ERROR).
     */
    @PostMapping
    public ResponseEntity<?> agregarPersona(@RequestBody PersonaDTO persona) {
        try {
            Persona personaCreada = personaService.crear(persona.aModelo());
            log.info("Persona creada con ID: {}", personaCreada.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(personaCreada);
        } catch (NombreNoValidoException | VidaErroneaException | DniNoValidoException | DniAsignadoException e) {
            return handleBadRequest(e);
        }
        catch (Exception e) {
            return handleInternalServerError(e);
        }
    }

    /**
     * Obtiene una persona por su ID.
     *
     * @param id ID de la persona a ser recuperada.
     * @return ResponseEntity con la persona recuperada y el estado HTTP 200 (OK).
     *         En caso de no encontrar la persona, retorna un ResponseEntity con un mensaje de error y el estado HTTP 404 (NOT FOUND).
     *         En caso de otros errores, retorna un ResponseEntity con un mensaje de error genérico y el estado HTTP 500 (INTERNAL SERVER ERROR).
     */
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

    /**
     * Actualiza los datos de una persona existente.
     *
     * @param id ID de la persona a ser actualizada.
     * @param persona PersonaDTO que representa los nuevos datos de la persona.
     * @return ResponseEntity con la persona actualizada y el estado HTTP 200 (OK).
     *         En caso de no encontrar la persona, retorna un ResponseEntity con un mensaje de error y el estado HTTP 404 (NOT FOUND).
     *         En caso de errores de validación, retorna un ResponseEntity con el mensaje de error y el estado HTTP 400 (BAD REQUEST).
     *         En caso de otros errores, retorna un ResponseEntity con un mensaje de error genérico y el estado HTTP 500 (INTERNAL SERVER ERROR).
     */
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

    /**
     * Elimina una persona por su ID.
     *
     * @param id ID de la persona a ser eliminada.
     * @return ResponseEntity con el mensaje de éxito y el estado HTTP 200 (OK).
     *         En caso de no encontrar la persona, retorna un ResponseEntity con un mensaje de error y el estado HTTP 404 (NOT FOUND).
     *         En caso de otros errores, retorna un ResponseEntity con un mensaje de error genérico y el estado HTTP 500 (INTERNAL SERVER ERROR).
     */
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

    /**
     * Maneja los errores de solicitud incorrecta (HTTP 400 Bad Request).
     *
     * @param e Excepción que indica el error de validación.
     * @return ResponseEntity con el mensaje de error y el estado HTTP 400 (Bad Request).
     */
    private ResponseEntity<?> handleBadRequest(RuntimeException e) {
        log.error("Error al procesar la solicitud", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * Maneja los errores de recurso no encontrado (HTTP 404 Not Found).
     *
     * @param e  Excepción que indica que el recurso no fue encontrado.
     * @param id ID del recurso no encontrado.
     * @return ResponseEntity con el mensaje de error y el estado HTTP 404 (Not Found).
     */
    private ResponseEntity<?> handleNotFound(RuntimeException e, String id) {
        log.warn("No se encontró información para ID: {}", id, e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * Maneja los errores internos del servidor (HTTP 500 Internal Server Error).
     *
     * @param e Excepción que indica un error interno del servidor.
     * @return ResponseEntity con el mensaje de error y el estado HTTP 500 (Internal Server Error).
     */
    private ResponseEntity<?> handleInternalServerError(Exception e) {
        log.error("Error interno del servidor al procesar solicitud", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
}