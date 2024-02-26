package com.capacitacion.infraestructura.api;

import com.capacitacion.domain.model.exception.DniNoValidoException;
import com.capacitacion.domain.model.exception.NombreNoValidoException;
import com.capacitacion.domain.model.exception.PersonaNoExisteException;
import com.capacitacion.domain.model.exception.VidaErroneaException;
import com.capacitacion.application.PersonaService;
import com.capacitacion.infraestructura.api.dto.PersonaDTO;
import com.capacitacion.domain.model.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;


@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    private static final Logger logger = LoggerFactory.getLogger(PersonaController.class);

    @Autowired
    private PersonaService personaService;

    @GetMapping
    public ResponseEntity<List<Persona>> obtenerTodasLasPersonas() {
        List<Persona> personas = personaService.recuperarTodos();//.stream().map( { persona -> PersonaDTO.desdeModelo(persona)});
        return ResponseEntity.ok(personas);
    }

    @PostMapping
    public ResponseEntity<?> agregarPersona(@RequestBody PersonaDTO persona) {
        try {
            Persona personaCreada = personaService.crear(persona.aModelo());
            logger.info("Persona creada con ID: {}", personaCreada.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(personaCreada);
        } catch (NombreNoValidoException e) {
            logger.error("Error al crear persona con ID: {}", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (VidaErroneaException e) {
            logger.error("Error al crear persona con ID: {}", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DniNoValidoException e) {
            logger.error("Error al crear persona con ID: {}", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e) {
            logger.error("Error al crear persona", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    @GetMapping(value ="/{id}", produces = "application/json")
    public ResponseEntity<?> obtenerPersonaPorId(@PathVariable Long id) {
        try {
            //PersonaDTO.desdeModelo(personaService.recuperar(id))
            Persona persona = personaService.recuperar(id);
            return ResponseEntity.ok().body(persona);
        } catch (PersonaNoExisteException e) {
            logger.error("Error al obtener persona por ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error interno del servidor al obtener persona por ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPersona(@PathVariable Long id, @RequestBody PersonaDTO persona) {
        try {
            Persona personaActualizada = personaService.actualizar(id, persona.aModelo());
            logger.info("Persona actualizada con ID: {}", id);
            return ResponseEntity.ok(personaActualizada);
        } catch (PersonaNoExisteException e) {
            logger.error("Error al actualizar persona con ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (NombreNoValidoException e) {
            logger.error("Error al actualizar persona con ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (VidaErroneaException e) {
            logger.error("Error al actualizar persona con ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al actualizar persona con ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPersona(@PathVariable Long id) {
        try {
            personaService.eliminar(id);
            logger.info("Persona eliminada con ID: {}", id);
            return ResponseEntity.ok().body("La persona fue eliminada exitosamente");
        } catch (PersonaNoExisteException e) {
            logger.error("Error al eliminar persona con ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al eliminar persona con ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }
}