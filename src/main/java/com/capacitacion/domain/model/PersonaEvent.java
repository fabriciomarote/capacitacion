package com.capacitacion.domain.model;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;


@Getter @Setter
public class PersonaEvent implements Serializable {
    private String nombre;

    /**
     * Constructor de PersonaEvent.
     *
     * @param nombre Nombre asociado al evento de Persona.
     */
    public PersonaEvent(String nombre) {
        this.nombre = nombre;
    }


}