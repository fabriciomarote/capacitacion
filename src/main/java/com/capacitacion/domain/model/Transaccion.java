package com.capacitacion.domain.model;

import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter
@Document(collection = "transacciones")
public class Transaccion {

    @Id
    private String id;
    @NotBlank
    @Size(max = 8)
    private String dniOrigen;
    @NotBlank
    @Size(max = 8)
    private String dniDestino;
    @NotBlank
    @DecimalMin(value = "1")
    private int monto;

    /**
     * Constructor de Transaccion.
     *
     * @param id         Identificador único de la transacción (puede ser nulo si se crea una nueva transacción).
     * @param dniOrigen  Número de DNI del remitente.
     * @param dniDestino Número de DNI del destinatario.
     * @param monto      Monto de la transacción.
     */
    public Transaccion(String id, String dniOrigen, String dniDestino, int monto) {
        this.id = id;
        this.dniOrigen = dniOrigen;
        this.dniDestino = dniDestino;
        this.monto = monto;
    }

    /**
     * Constructor por defecto de Transaccion.
     */
    public Transaccion() {}

}
