package com.capacitacion.application;

import com.capacitacion.domain.model.Persona;

import java.util.List;


public interface PersonaService {
    public Persona crear(Persona persona);
    public Persona actualizar(Long idPersona, Persona persona);
    public Persona recuperar(Long idPersona);
    public Persona obtenerPorDni(String dniPersona);
    public void eliminar(Long idPersona);
    public List<Persona> recuperarTodos();
    public void realizarTransaccion(String dni1, String dni2, int monto);
}
