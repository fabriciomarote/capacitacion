package com.capacitacion.infraestructura.api.dto;

import com.capacitacion.domain.model.Transaccion;

public class TransaccionDTO {
    private String id;
    private String dniOrigen;
    private String dniDestino;
    private int monto;

    /**
     * Constructor de TransaccionDTO.
     *
     * @param id         Identificador único de la transacción.
     * @param dniOrigen  DNI del remitente en la transacción.
     * @param dniDestino DNI del destinatario en la transacción.
     * @param monto      Monto de la transacción.
     */
    public TransaccionDTO(String id, String dniOrigen, String dniDestino, int monto) {
        this.id = id;
        this.dniOrigen = dniOrigen;
        this.dniDestino = dniDestino;
        this.monto = monto;
    }

    /**
     * Método estático para crear un objeto TransaccionDTO a partir de un objeto Transaccion.
     *
     * @param transaccion Objeto Transaccion del cual se extraen los datos.
     * @return Objeto TransaccionDTO creado a partir del modelo.
     */
    public static TransaccionDTO desdeModelo(Transaccion transaccion) {
        return new TransaccionDTO(
                transaccion.getId(),
                transaccion.getDniOrigen(),
                transaccion.getDniDestino(),
                transaccion.getMonto()
        );
    }

    /**
     * Convierte el objeto TransaccionDTO a un objeto Transaccion.
     *
     * @return Objeto Transaccion creado a partir de los datos en el DTO.
     */
    public Transaccion aModelo() {
        Transaccion transaccion = new Transaccion();
        transaccion.setId(this.id);
        transaccion.setDniOrigen(this.dniOrigen);
        transaccion.setDniDestino(this.dniDestino);
        transaccion.setMonto(this.monto);
        return transaccion;
    }
}
