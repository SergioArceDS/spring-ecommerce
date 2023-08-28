package com.sga.springecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Usuario {
    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NonNull
    private String nombre;
    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private String direccion;
    @NonNull
    private String telefono;
    @NonNull
    private String tipo;
    @NonNull
    private String password;

    @OneToMany(mappedBy = "usuario")
    private List<Producto> productos;

    @OneToMany(mappedBy = "usuario")
    private List<Orden> ordenes;


}
