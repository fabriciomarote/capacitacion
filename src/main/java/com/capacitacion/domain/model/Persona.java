package com.capacitacion.domain.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter
@Document(collection = "personas")
public class Persona {

    @Id
    private String id;
    private String nombre;
    private int edad;
    private String dni;
    private int creditos = 100;
    private String direccion;

    /**
     * Constructor de Persona.
     *
     * @param id     Identificador único de la persona (puede ser nulo si se crea una nueva persona).
     * @param nombre Nombre de la persona.
     * @param edad   Edad de la persona.
     * @param dni    Número de DNI de la persona.
     */
    public Persona(@Nullable String id, @Nullable String nombre, int edad, String dni) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.dni = dni;
    }

    /**
     * Constructor por defecto de Persona.
     */
    public Persona() {}

    /**
     * Resta créditos a la persona por una transacción.
     *
     * @param monto Monto a restar de los créditos.
     */
    public void restarCreditoPorTransaccion(int monto) {
        this.setCreditos(this.creditos - monto);
    }

    /**
     * Aumenta créditos a la persona por una transacción.
     *
     * @param monto Monto a sumar a los créditos.
     */
    public void aumentarCreditoPorTransaccion(int monto) {
        this.setCreditos(this.creditos + monto);
    }
}
