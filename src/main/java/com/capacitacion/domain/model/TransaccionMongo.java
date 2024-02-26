package com.capacitacion.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transacciones")
public class TransaccionMongo {
    @Id
    private Long id;
    private String dniOrigen;
    private String dniDestino;
    private int monto;

    public TransaccionMongo(Long id, String dniOrigen, String dniDestino, int monto) {
        this.id = id;
        this.dniOrigen = dniOrigen;
        this.dniDestino = dniDestino;
        this.monto = monto;
    }
}