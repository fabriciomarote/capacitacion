package com.capacitacion.domain.model;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter @Setter
public class TransaccionEvent implements Serializable {

    private String transaccionId;

    /**
     * Constructor de TransaccionEvent.
     *
     * @param transaccionId Identificador único de la transacción.
     */
    public TransaccionEvent(String transaccionId) {
        this.transaccionId = transaccionId;
    }
}
