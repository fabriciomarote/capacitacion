package com.capacitacion.domain.model.exception;

public class TransaccionNoExisteException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Message Error: La transaccion con el id proporcionado no existe.";
    }

    private static final long serialVersionUID = 1L;
}