package com.capacitacion.domain.model.exception;

public class NombreNoValidoException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Message Error: El nombre no puede ser vacio o nulo";
    }

    private static final long serialVersionUID = 1L;
}