package com.capacitacion.domain.model.exception;

public class MontoNoValidoException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Message Error: El monto no puede ser menor o igual a 0";
    }

    private static final long serialVersionUID = 1L;
}