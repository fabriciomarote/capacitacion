package com.capacitacion.infraestructura.api;

import com.capacitacion.domain.model.exception.*;
import com.capacitacion.domain.application.PersonaService;
import com.capacitacion.infraestructura.api.dto.PersonaDTO;
import com.capacitacion.domain.model.Persona;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Configuration
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
    @Operation(summary = "Obtiene todas las personas persistidas en la base de datos",
            description = "Recupera la lista completa de personas almacenadas en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obtiene una lista de personas",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema( schema = @Schema(implementation = Persona.class)))})})
    @GetMapping
    public ResponseEntity<List<Persona>> obtenerTodasLasPersonas() {
        try {
            List<Persona> personas = personaService.recuperarTodos();
            log.debug("Obteniendo todas las personas: {}", personas);

            if (personas.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(personas);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Elimina todas las personas de la base de datos.
     *
     * @return ResponseEntity con el mensaje de éxito y el estado HTTP 200 (OK).
     */
    @Operation(summary = "Borra a todas las personas persistidas de la base de datos",
            description = "Elimina de manera permanente todas las personas almacenadas en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Las personas fueron eliminadas exitosamente",
                    content = @Content)})
    @DeleteMapping
    public ResponseEntity<?> eliminarTodo() {
        try {
            personaService.eliminarTodo();
            log.warn("¡Atención! Se eliminaron todas las personas.");
            return ResponseEntity.ok().body("Las personas fueron eliminadas exitosamente");
        } catch (Exception ex) {
            log.error("Error al eliminar todas las personas.", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Agrega una nueva persona a la base de datos.
     *
     * @param persona PersonaDTO que representa los datos de la persona a ser creada.
     * @return ResponseEntity con la persona creada y el estado HTTP 201 (CREATED).
     *         En caso de errores de validación, retorna un ResponseEntity con el mensaje de error y el estado HTTP 400 (BAD REQUEST).
     *         En caso de otros errores, retorna un ResponseEntity con un mensaje de error genérico y el estado HTTP 500 (INTERNAL SERVER ERROR).
     */
    @Operation(summary = "Agrega una nueva persona a la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agrega una nueva persona",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Persona.class))}),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> agregarPersona(@RequestBody PersonaDTO persona) {
        try {
            Persona personaCreada = personaService.crear(persona.aModelo());
            log.info("Persona creada con ID: {}", personaCreada.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(personaCreada);
        } catch (NombreNoValidoException e) {
            return handleBadRequest("Nombre no válido: " + e.getMessage());
        } catch (VidaErroneaException e) {
            return handleBadRequest("Vida erronea: " + e.getMessage());
        } catch (DniNoValidoException e) {
            return handleBadRequest("DNI no válido: " + e.getMessage());
        } catch (DniAsignadoException e) {
            return handleBadRequest("DNI ya asignado: " + e.getMessage());
        } catch (Exception e) {
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
    @Operation(summary = "Obtiene una persona de la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Persona encontrada exitosamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Persona.class))}),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Persona no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content) })
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
    @Operation(summary = "Actualiza una persona de la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Actualiza una persona",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Persona.class))}),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta o datos no válidos",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Persona no encontrada para el ID especificado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content) })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPersona(@PathVariable String id, @RequestBody PersonaDTO persona) {
        try {
            Persona personaActualizada = personaService.actualizar(id, persona.aModelo());
            log.info("Persona actualizada con ID: {}", id);
            return ResponseEntity.ok(personaActualizada);
        } catch (PersonaNoExisteException e) {
            return handleNotFound(e, id);
        } catch (NombreNoValidoException e) {
            return handleBadRequest("Nombre no válido: " + e.getMessage());
        } catch (VidaErroneaException e) {
            return handleBadRequest("Vida erronea: " + e.getMessage());
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
    @Operation(summary = "Borra a la persona de la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La personas fuer eliminada exitosamente",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontró la persona para el ID especificado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)})
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
     * @param errorMessage Mensaje de error detallado.
     * @return ResponseEntity con el mensaje de error y el estado HTTP 400 (Bad Request).
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<?> handleBadRequest(String errorMessage) {
        log.error("Error al procesar la solicitud: {}", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    /**
     * Maneja los errores de recurso no encontrado (HTTP 404 Not Found).
     *
     * @param e  Excepción que indica que el recurso no fue encontrado.
     * @param id ID del recurso no encontrado.
     * @return ResponseEntity con el mensaje de error y el estado HTTP 404 (Not Found).
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
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
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<?> handleInternalServerError(Exception e) {
        log.error("Error interno del servidor al procesar solicitud", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
}