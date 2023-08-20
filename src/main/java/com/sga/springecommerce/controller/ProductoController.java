package com.sga.springecommerce.controller;

import com.sga.springecommerce.model.Producto;
import com.sga.springecommerce.model.Usuario;
import com.sga.springecommerce.service.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    private final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    @GetMapping("")
    public String show(){

        return "productos/show";
    }

    @GetMapping("crear")
    public String create(){

        return "productos/create";
    }

    @PostMapping("/guardar")
    public String save(Producto producto){
        logger.info("Este es el objeto producto {}", producto);
        Usuario usuario = new Usuario(1, "", "", "", "", "", "", "");

        producto.setUsuario(usuario);
        productoService.save(producto);
        return "redirect:/productos";
    }
}
