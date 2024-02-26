package com.capacitacion.domain.model.exception;

public class DniNoValidoException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Message Error: El dni debe contener solo 8 caracteres";
    }

    private static final long serialVersionUID = 1L;
}