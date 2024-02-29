package com.capacitacion.infraestructura.api.dto;

import com.capacitacion.domain.model.Persona;

public class PersonaDTO {
    private String id;
    private String nombre;
    private int edad;
    private String dni;
    private int creditos;

    /**
     * Constructor de PersonaDTO.
     *
     * @param id       Identificador único de la persona.
     * @param nombre   Nombre de la persona.
     * @param edad     Edad de la persona.
     * @param dni      Documento Nacional de Identidad (DNI) de la persona.
     * @param creditos Créditos disponibles para la persona.
     */
    public PersonaDTO(String id, String nombre, int edad, String dni, int creditos) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.dni = dni;
        this.creditos = 100;
    }

    /**
     * Método estático para crear un objeto PersonaDTO a partir de un objeto Persona.
     *
     * @param persona Objeto Persona del cual se extraen los datos.
     * @return Objeto PersonaDTO creado a partir del modelo.
     */
    public static PersonaDTO desdeModelo(Persona persona) {
        return new PersonaDTO(
                persona.getId(),
                persona.getNombre(),
                persona.getEdad(),
                persona.getDni(),
                persona.getCreditos()
        );
    }

    /**
     * Convierte el objeto PersonaDTO a un objeto Persona.
     *
     * @return Objeto Persona creado a partir de los datos en el DTO.
     */
    public Persona aModelo() {
        Persona persona = new Persona();
        persona.setId(this.id);
        persona.setNombre(this.nombre);
        persona.setEdad(this.edad);
        persona.setDni(this.dni);
        persona.setCreditos(this.creditos);
        return persona;
    }
}
