package com.capacitacion.domain.model.exception;

public class VidaErroneaException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Message Error: Debe tener una edad valida(de 1 a 100).";
    }

    private static final long serialVersionUID = 1L;
}