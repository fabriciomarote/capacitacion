package com.capacitacion.domain.model;

import java.io.Serializable;

public class PersonaEvent implements Serializable {
    private String nombre;

    public PersonaEvent(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}