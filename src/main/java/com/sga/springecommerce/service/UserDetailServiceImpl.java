package com.sga.springecommerce.service;

import com.sga.springecommerce.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    HttpSession session;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOptional = usuarioService.findByEmail(username);

        if(usuarioOptional.isPresent()){
            logger.info("Este es el id del usuario: {}", usuarioOptional.get().getId());
            session.setAttribute("idUsuario", usuarioOptional.get().getId());
            Usuario usuario = usuarioOptional.get();
            return User.builder()
                    .username(usuario.getNombre())
                    .password(usuario.getPassword())
                    .roles(usuario.getTipo())
                    .build();
        }else{
            throw new UsernameNotFoundException("Usuario no encontrado.");
        }
    }
}
