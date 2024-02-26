package com.capacitacion.domain.application;

import com.capacitacion.domain.model.Transaccion;

public interface TransaccionService {
    public Transaccion realizarTransaccion(Transaccion transaccion);
    public void eliminarTodo();
}
