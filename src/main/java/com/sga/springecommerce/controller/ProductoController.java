package com.sga.springecommerce.controller;

import com.sga.springecommerce.model.Producto;
import com.sga.springecommerce.model.Usuario;
import com.sga.springecommerce.service.IProductoService;
import com.sga.springecommerce.service.IUsuarioService;
import com.sga.springecommerce.service.UploadFileService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private UploadFileService uploadFileService;

    private final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    @GetMapping("")
    public String show(Model model){
        model.addAttribute("productos", productoService.findAll());
        return "productos/show";
    }

    @GetMapping("crear")
    public String create(){

        return "productos/create";
    }

    @PostMapping("/guardar")
    public String save(Producto producto, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
        logger.info("Este es el objeto producto {}", producto);
        Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idUsuario").toString())).get();
        producto.setUsuario(usuario);

        if(producto.getId() == null){
            String nombreImagen = uploadFileService.saveImage(file);
            producto.setImagen(nombreImagen);
        }else{

        }

        productoService.save(producto);
        return "redirect:/productos";
    }

    @GetMapping("/editar/{id}")
    public String edit(@PathVariable Integer id, Model model){
        Producto producto;
        Optional<Producto> optionalProducto = productoService.get(id);
        producto = optionalProducto.get();

        model.addAttribute("producto", producto);
        return "productos/edit";
    }

    @PostMapping("/update")
    public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
        Producto p;
        p = productoService.get(producto.getId()).get();

        if(file.isEmpty()){
            producto.setImagen(p.getImagen());
        }else{
            if(!p.getImagen().equals("default.jpg")){
                uploadFileService.deleteImage(p.getImagen());
            }

            String nombreImagen = uploadFileService.saveImage(file);
            producto.setImagen(nombreImagen);
        }
        producto.setUsuario(p.getUsuario());
        productoService.save(producto);
        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String delete(@PathVariable Integer id){
        Producto p;
        p = productoService.get(id).get();

        if(!p.getImagen().equals("default.jpg")){
            uploadFileService.deleteImage(p.getImagen());
        }

        productoService.delete(id);

        return "redirect:/productos";
    }
}
