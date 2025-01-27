package com.reading.sala_de_leitura.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Usuario")
@Table(name = "usuarios")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usuario;
}
