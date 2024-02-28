package com.capacitacion.domain.model;

import java.io.Serializable;

public class TransaccionEvent implements Serializable {
    private String transaccionId;

    public TransaccionEvent(String transaccionId) {
        this.transaccionId = transaccionId;
    }

    public String getTransaccionId() {
        return transaccionId;
    }

    public void setTransaccionId(String transaccionId) {
        this.transaccionId = transaccionId;
    }


}
