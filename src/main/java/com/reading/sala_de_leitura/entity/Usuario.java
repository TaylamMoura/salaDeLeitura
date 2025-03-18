package com.reading.sala_de_leitura.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

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

    private String email;

    @Column(name = "senha_hash")
    private String senhaHash;

    @Column(name = "data_criacao")
    private Timestamp dataCriacao;
}
