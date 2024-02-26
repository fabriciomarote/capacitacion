package com.capacitacion.domain.model;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transacciones")
public class Transaccion {
    @Id
    private String id;
    private String dniOrigen;
    private String dniDestino;
    private int monto;

    public Transaccion(String id, String dniOrigen, String dniDestino, int monto) {
        this.id = id;
        this.dniOrigen = dniOrigen;
        this.dniDestino = dniDestino;
        this.monto = monto;
    }

    public Transaccion() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDniOrigen() {
        return dniOrigen;
    }

    public void setDniOrigen(String dniOrigen) {
        this.dniOrigen = dniOrigen;
    }

    public String getDniDestino() {
        return dniDestino;
    }

    public void setDniDestino(String dniDestino) {
        this.dniDestino = dniDestino;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }
}
