package com.capacitacion.infraestructura.listener;

import com.capacitacion.domain.application.PersonaService;
import com.capacitacion.domain.model.Persona;
import com.capacitacion.domain.model.Sucursal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class PersonaEventListener {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private PersonaService personaService;

    /**
     * Maneja el evento de alta de persona recibido desde Kafka.
     *
     * @param nombrePersona Nombre de la persona recibido en el evento.
     * @throws JsonProcessingException Excepción lanzada en caso de problemas con el procesamiento JSON.
     */
    @KafkaListener(topics = "alta-persona-topic", groupId = "group-id")
    public void escucharEventoAltaPersona(String nombrePersona) throws JsonProcessingException {

        // Procesa el nombre de la persona desde el evento.
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(nombrePersona);
        String nombre = jsonNode.get("nombre").asText();

        // Obtiene la lista de sucursales desde la API mock.
        List<Sucursal> data = obtenerDatos();
        List<Sucursal> sucursales = data.stream()
                .filter(sucursal -> sucursal.getNombre().equals(nombre))
                .toList();

        // Si hay sucursales coincidentes, actualiza la dirección de la persona.
        if(!sucursales.isEmpty()) {
            Persona persona = personaService.obtenerPorNombre(nombre);
            actualizarDireccionPersona(persona, sucursales.get(0).getDireccion());
        }
    }

    /**
     * Obtiene la lista de sucursales desde la API mock.
     *
     * @return Lista de sucursales.
     */
    private List<Sucursal> obtenerDatos() {
        ResponseEntity<Sucursal[]> response = restTemplate.getForEntity(
                "https://627303496b04786a09002b27.mockapi.io/mock/sucursales",
                Sucursal[].class
        );
        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }

    /**
     * Actualiza la dirección de una persona.
     *
     * @param persona Persona a la que se le actualizará la dirección.
     * @param direccion Nueva dirección.
     */
    private void actualizarDireccionPersona(Persona persona, String direccion) {
        persona.setDireccion(direccion);
        personaService.actualizar(persona.getId(), persona);
    }
}

