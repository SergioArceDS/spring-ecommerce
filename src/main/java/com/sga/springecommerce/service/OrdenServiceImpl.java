package com.sga.springecommerce.service;

import com.sga.springecommerce.model.Orden;
import com.sga.springecommerce.model.Usuario;
import com.sga.springecommerce.repository.IOrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenServiceImpl implements IOrdenService{

    @Autowired
    private IOrdenRepository ordenRepository;
    @Override
    public Orden saveOrden(Orden orden) {
        return ordenRepository.save(orden);
    }

    @Override
    public List<Orden> findAll() {
        return ordenRepository.findAll();
    }

    public String generarNumeroOrden(){
        int numero = 0;
        String numeroConcatenado = "";

        List<Orden> ordenes = this.findAll();
        List<Integer> numeros = new ArrayList<>();

        ordenes.stream().forEach(orden -> numeros.add(Integer.parseInt(orden.getNumero())));

        if(ordenes.isEmpty()){
            numero = 1;
        }else{
            numero = numeros.stream().max(Integer::compare).get();
            numero++;
        }

        if(numero < 10){
            numeroConcatenado = "000000000" + String.valueOf(numero);
        }else if(numero < 100){
            numeroConcatenado = "00000000" + String.valueOf(numero);
        } else if (numero < 1000){
            numeroConcatenado = "0000000" + String.valueOf(numero);
        }

        return numeroConcatenado;
    }

    @Override
    public List<Orden> findByUsuario(Usuario usuario) {
        return ordenRepository.findByUsuario(usuario);
    }

    @Override
    public Optional<Orden> findById(Integer id) {
        return ordenRepository.findById(id);
    }
}
