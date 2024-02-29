package com.capacitacion.domain.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "personas")
public class Persona {
    @Id
    private String id;

    private String nombre;
    private int edad;
    private String dni;
    private int creditos = 100;
    private String direccion;


    public Persona(@Nullable String id, @Nullable String nombre, int edad, String dni) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.dni = dni;
    }

    public Persona() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String nuevaDireccion) {
        this.direccion = nuevaDireccion;
    }

    public void restarCreditoPorTransaccion(int monto) {
        this.setCreditos(this.creditos - monto);
    }

    public void aumentarCreditoPorTransaccion(int monto) {
        this.setCreditos(this.creditos + monto);
    }
}
