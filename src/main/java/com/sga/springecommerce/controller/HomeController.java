package com.sga.springecommerce.controller;

import com.sga.springecommerce.model.DetalleOrden;
import com.sga.springecommerce.model.Orden;
import com.sga.springecommerce.model.Producto;
import com.sga.springecommerce.model.Usuario;
import com.sga.springecommerce.service.IDetalleOrdenService;
import com.sga.springecommerce.service.IOrdenService;
import com.sga.springecommerce.service.IUsuarioService;
import com.sga.springecommerce.service.IProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private IProductoService productoService;

    @Autowired
    IUsuarioService usuarioService;

    @Autowired
    IOrdenService ordenService;

    @Autowired
    IDetalleOrdenService detalleOrdenService;

    List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();

    Orden orden = new Orden();

    @GetMapping("")
    public String home(Model model){
        List<Producto> productos = productoService.findAll();
        model.addAttribute("productos", productos);

        return "usuario/home";
    }

    @GetMapping("productohome/{id}")
    public String productoHome(@PathVariable Integer id, Model model){
        logger.info("Id del producto: {}", id);
        Producto producto;
        Optional<Producto> productoOptional = productoService.get(id);
        producto = productoOptional.get();

        model.addAttribute("producto", producto);

        return "usuario/productohome";
    }

    @PostMapping("/cart")
    public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model){
        DetalleOrden detalleOrden = new DetalleOrden();
        Producto producto;
        double sumaTotal = 0;

        Optional<Producto> optionalProducto = productoService.get(id);
        logger.info("Producto añadido {}", optionalProducto.get().getNombre());

        producto = optionalProducto.get();
        detalleOrden.setCantidad(cantidad);
        detalleOrden.setPrecio(producto.getPrecio());
        detalleOrden.setNombre(producto.getNombre());
        detalleOrden.setTotal(producto.getPrecio() * cantidad);
        detalleOrden.setProducto(producto);

        //Validacion para que el mismo producto no se añada mas de una vez
        Integer idProducto = producto.getId();
        boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);

        if(!ingresado){
            detalles.add(detalleOrden);
        }

        sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
        orden.setTotal(sumaTotal);
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);
        return "usuario/carrito";
    }

    @GetMapping("/delete/cart/{id}")
    public String deleteProductFromCart(@PathVariable Integer id, Model model){

        List<DetalleOrden> ordenesNuevas = new ArrayList<DetalleOrden>();

        for(DetalleOrden detalleOrden : detalles){
            if(detalleOrden.getProducto().getId() != id){
                ordenesNuevas.add(detalleOrden);
            }
        }

        detalles = ordenesNuevas;

        double sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();;
        orden.setTotal(sumaTotal);
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);

        return "usuario/carrito";
    }

    @GetMapping("/getCart")
    public String getCart(Model model){
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);

        return "/usuario/carrito";
    }

    @GetMapping("/order")
    public String order(Model model){

        Usuario usuario = usuarioService.findById(1).get();

        model.addAttribute("usuario", usuario);
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);

        return "usuario/resumenorden";
    }

    @GetMapping("/saveOrder")
    public String saveOrder(){
        Date fechaCreacion = new Date();
        orden.setFechaCreacion(fechaCreacion);
        orden.setNumero(ordenService.generarNumeroOrden());

        Usuario usuario = usuarioService.findById(1).get();
        orden.setUsuario(usuario);

        ordenService.saveOrden(orden);

        for (DetalleOrden dt : detalles){
            dt.setOrden(orden);
            detalleOrdenService.saveDetalleOrden(dt);
        }

        //Limpiar la lista y la orden
        orden = new Orden();
        detalles.clear();

        return "redirect:/";
    }

    @PostMapping("/search")
    public String searchProduct(@RequestParam String nombreBusqueda, Model model){
        logger.info("Se esta realizando la busqueda para {}", nombreBusqueda);
        List<Producto> productos = productoService.findAll()
                .stream()
                .filter(producto -> producto.getNombre().contains(nombreBusqueda))
                .collect(Collectors.toList());

        model.addAttribute("productos", productos);
        return "usuario/home";
    }
}
