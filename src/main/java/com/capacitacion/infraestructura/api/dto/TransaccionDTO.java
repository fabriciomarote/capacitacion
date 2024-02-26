package com.capacitacion.infraestructura.api.dto;

import com.capacitacion.domain.model.Transaccion;

public class TransaccionDTO {
    private Long id;
    private String dniOrigen;
    private String dniDestino;
    private int monto;

    public TransaccionDTO(Long id, String dniOrigen, String dniDestino, int monto) {
        this.id = id;
        this.dniOrigen = dniOrigen;
        this.dniDestino = dniDestino;
        this.monto = monto;
    }

    public static TransaccionDTO desdeModelo(Transaccion transaccion) {
        return new TransaccionDTO(
                transaccion.getId(),
                transaccion.getDniOrigen(),
                transaccion.getDniDestino(),
                transaccion.getMonto()
        );
    }

    public Transaccion aModelo() {
        Transaccion transaccion = new Transaccion();
        transaccion.setId(this.id);
        transaccion.setDniOrigen(this.dniOrigen);
        transaccion.setDniDestino(this.dniDestino);
        transaccion.setMonto(this.monto);
        return transaccion;
    }
}
