package com.capacitacion.domain.model.exception;

public class MontoInsuficienteException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Message Error: El monto es mayor al credito disponible de la persona correspondiente al dniOrigen";
    }

    private static final long serialVersionUID = 1L;
}