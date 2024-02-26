package com.capacitacion.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dniOrigen;
    private String dniDestino;
    private int monto;

    public Transaccion(Long id, String dniOrigen, String dniDestino, int monto) {
        this.id = id;
        this.dniOrigen = dniOrigen;
        this.dniDestino = dniDestino;
        this.monto = monto;
    }

    public Transaccion() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
