package com.capacitacion.application;

import com.capacitacion.domain.model.PersonaMongo;

import java.util.List;

public interface PersonaMongoService {

    public PersonaMongo crear(PersonaMongo persona);

    public PersonaMongo actualizar(PersonaMongo persona);
    public PersonaMongo recuperar(Long idPersona);
    public void eliminar(Long idPersona);
    public List<PersonaMongo> recuperarTodos();
}
