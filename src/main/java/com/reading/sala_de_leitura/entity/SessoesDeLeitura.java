package com.reading.sala_de_leitura.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

@Entity(name = "SessoesDeLeitura")
@Table(name = "sessoes")

public class SessoesDeLeitura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "livro_id")
    private Livro livro;

    @Column(name = "inicio_sessao")
    private LocalDateTime inicioSessao;

    @Column(name = "fim_sessao")
    private  LocalDateTime fimSessao;

    @Column(name = "pagina_inicial")
    private int paginaInicial;

    @Column(name = "pagina_final")
    private int paginaFinal;

    @Column(name = "tempo_leitura")
    private Integer tempoLeitura;

    @Column(name = "url_capa")
    private String urlCapa;

    //Antes de salvar uma nova SessoesDeLeitura, este método define inicioSessao com a data e hora atuais
    @PrePersist
    protected void onCreate(){
        inicioSessao = LocalDateTime.now();
    }

}

