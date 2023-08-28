package com.sga.springecommerce.service;

import com.sga.springecommerce.model.Orden;
import com.sga.springecommerce.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface IOrdenService {
    Orden saveOrden(Orden orden);

    List<Orden> findAll();

    String generarNumeroOrden();

    List<Orden> findByUsuario(Usuario usuario);

    Optional<Orden> findById(Integer id);
}
