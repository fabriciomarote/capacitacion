package com.capacitacion.domain.model;

import lombok.Getter;

@Getter
public class Sucursal {

    private String nombre;
    private String direccion;

    /**
     * Constructor de Sucursal.
     *
     * @param nombre    Nombre de la sucursal.
     * @param direccion Dirección de la sucursal.
     */
    public Sucursal(String nombre, String direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
    }
}
