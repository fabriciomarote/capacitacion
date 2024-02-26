package com.capacitacion.domain.application;

import com.capacitacion.domain.model.Persona;

import java.util.List;


public interface PersonaService {
    public Persona crear(Persona persona);
    public Persona actualizar(String idPersona, Persona persona);
    public Persona recuperar(String idPersona);
    public Persona obtenerPorDni(String dniPersona);
    public void eliminar(String idPersona);
    public List<Persona> recuperarTodos();
    public void realizarTransaccion(String dni1, String dni2, int monto);
    public void eliminarTodo();
}
