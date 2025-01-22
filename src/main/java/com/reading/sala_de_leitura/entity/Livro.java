package com.reading.sala_de_leitura.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Livro")
@Table(name = "livros")

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")

public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String autor;
    private int paginas;

    @Column(name = "url_capa")
    private String urlCapa;

    @Column(name = "ano_publicacao")
    private int anoPublicacao;

}
