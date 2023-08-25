package com.sga.springecommerce.service;

import com.sga.springecommerce.model.Orden;

import java.util.List;

public interface IOrdenService {
    Orden saveOrden(Orden orden);

    List<Orden> findAll();
}
