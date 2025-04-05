package com.reading.sala_de_leitura.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @NotEmpty(message = "Defina um nome de usuário!")
    private String usuario;

    @NotEmpty(message = "O email é obrigatório!")
    @Email(message = "Escreva um email válido!")
    private String email;

    @NotEmpty(message = "Defina uma senha.")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 números")
    @Column(name = "senha_hash")
    private String senha;

    @Column(name = "data_criacao", updatable = false)
    @CreationTimestamp
    private Timestamp dataCriacao;

    @NotEmpty(message = "Digite seu nome!")
    private String nome;

    @Column(name = "data_aniversario")
    @NotNull(message = "Digite sua data de aniversário.")
    private LocalDate dataNascimento;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Livro> livros = new ArrayList<>();
}
