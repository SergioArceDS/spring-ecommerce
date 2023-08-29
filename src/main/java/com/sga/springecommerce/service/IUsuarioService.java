package com.sga.springecommerce.service;

import com.sga.springecommerce.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    Optional<Usuario> findById(Integer id);
    Usuario save(Usuario usuario);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findAll();
}
