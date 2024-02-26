package com.capacitacion.infraestructura.api.dto;

import com.capacitacion.domain.model.Persona;

public class PersonaDTO {
    private Long id;
    private String nombre;
    private int edad;
    private String dni;
    private int creditos;

    public PersonaDTO(Long id, String nombre, int edad, String dni, int creditos) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.dni = dni;
        this.creditos = 100;
    }


    public static PersonaDTO desdeModelo(Persona persona) {
        return new PersonaDTO(
                persona.getId(),
                persona.getNombre(),
                persona.getEdad(),
                persona.getDni(),
                persona.getCreditos()
        );
    }

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
