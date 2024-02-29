package com.capacitacion.domain.application;

import com.capacitacion.domain.model.Persona;

import java.util.List;


public interface PersonaService {
    public Persona crear(Persona persona);
    public Persona actualizar(String idPersona, Persona persona);
    public Persona recuperar(String idPersona);
    public Persona obtenerPorDni(String dniPersona);
    public Persona obtenerPorNombre(String nombrePersona);
    public void eliminar(String idPersona);
    public List<Persona> recuperarTodos();
    public void eliminarTodo();
}
