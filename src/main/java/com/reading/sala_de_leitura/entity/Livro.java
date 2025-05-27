package com.reading.sala_de_leitura.entity;

import com.reading.sala_de_leitura.service.AtualizarLivro;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Livro")
@Table(name = "livros")

@Getter
@Setter
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

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "livro_finalizado", nullable = false)
    private Boolean livroFinalizado = false;

    public void atualizar(AtualizarLivro dados) {
        if(dados.paginas() != 0){
            this.paginas = dados.paginas();
        }

        if (dados.anoPublicacao() != 0 && String.valueOf(dados.anoPublicacao()).length() <= 4){
            this.anoPublicacao = dados.anoPublicacao();
        }
    }
}
