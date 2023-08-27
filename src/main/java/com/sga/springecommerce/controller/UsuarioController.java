package com.sga.springecommerce.controller;

import com.sga.springecommerce.model.Usuario;
import com.sga.springecommerce.service.IUsuarioService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping("/registro")
    public String registroUsuario(){
        return "usuario/registro";
    }

    @PostMapping("/save")
    public String guardarUsuario(Usuario usuario){
        usuario.setTipo("USER");
        usuarioService.save(usuario);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(){
        return "usuario/login";
    }

    @PostMapping("/acceder")
    public String acceder(Usuario usuario, HttpSession session){

        Optional<Usuario> user = usuarioService.findByEmail(usuario.getEmail());

        if(user.isPresent()){
            session.setAttribute("idUsuario", user.get().getId());
            if(user.get().getTipo().equals("ADMIN")){
                return "redirect:/administrador";
            }else{
                return "redirect:/";
            }
        }else{
            logger.info("El Usuario no existe");
        }

        return "redirect:/";
    }

    @GetMapping("/compras")
    public String obtenerCompras(Model model, HttpSession session){

        model.addAttribute("session", session.getAttribute("idUsuario"));
        return "usuario/compras";
    }
}
