package com.capacitacion.application.impl;

import com.capacitacion.application.TransaccionMongoService;
import com.capacitacion.domain.model.TransaccionMongo;
import com.capacitacion.domain.repository.TransaccionMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransaccionMongoServiceImpl implements TransaccionMongoService {
    @Autowired
    private final TransaccionMongoRepository transaccionMongoRepository;

    public TransaccionMongoServiceImpl(TransaccionMongoRepository transaccionMongoRepository) {
        this.transaccionMongoRepository = transaccionMongoRepository;
    }

    @Override
    public TransaccionMongo crear(TransaccionMongo transaccion) {
        return transaccionMongoRepository.save(transaccion);
    }
}
