package com.capacitacion.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "personas")
public class PersonaMongo {
    @Id
    private Long id;
    private String nombre;
    private int edad;
    private String dni;
    private int creditos;

    public PersonaMongo(Long id, String nombre, int edad, String dni) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.dni = dni;
        this.creditos = 100;
    }

}
