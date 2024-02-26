package com.capacitacion.domain.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private int edad;
    private String dni;
    private int creditos = 100;


    public Persona(@Nullable Long id, @Nullable String nombre, int edad, String dni) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.dni = dni;
    }

    public Persona() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nuevoNombre) {
        this.nombre = nuevoNombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int nuevaEdad) {
        this.edad = nuevaEdad;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String nuevoDni) {
        this.dni = nuevoDni;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int nuevosCreditos) {
        this.creditos = nuevosCreditos;
    }

    public void restarCreditoPorTransaccion(int monto) {
        this.creditos -= monto;
    }

    public void aumentarCreditoPorTransaccion(int monto) {
        this.creditos += monto;
    }
}
