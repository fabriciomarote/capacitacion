package com.capacitacion.domain.model.exception;

public class DniRepetidoException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Message Error: El dniOrigen y DniDestino no pueden ser iguales, la transaccion debe realizarse a otra persona";
    }

    private static final long serialVersionUID = 1L;
}