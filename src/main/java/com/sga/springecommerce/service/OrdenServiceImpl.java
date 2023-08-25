package com.sga.springecommerce.service;

import com.sga.springecommerce.model.Orden;
import com.sga.springecommerce.repository.IOrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdenServiceImpl implements IOrdenService{

    @Autowired
    private IOrdenRepository ordenRepository;
    @Override
    public Orden saveOrden(Orden orden) {
        return ordenRepository.save(orden);
    }
}
