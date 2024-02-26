package com.capacitacion.domain.model.exception;

public class PersonaNoExisteException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Message Error: La persona con el id proporcionado no existe.";
    }

    private static final long serialVersionUID = 1L;
}