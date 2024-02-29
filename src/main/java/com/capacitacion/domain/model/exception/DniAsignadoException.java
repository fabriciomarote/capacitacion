package com.capacitacion.domain.model.exception;

public class DniAsignadoException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Message Error: El dni ya se encuentra asignado a una persona";
    }

    private static final long serialVersionUID = 1L;
}