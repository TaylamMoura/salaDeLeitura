package com.reading.sala_de_leitura.dto;

import com.reading.sala_de_leitura.entity.SessoesDeLeitura;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.sql.Time;

public record SessaoDTO(
        @NotNull Long usuarioId,
        @NotNull Long livroId,
        @Min(1) int paginasLidas,
        int tempoLeitura,
        int paginaFinal,
        String urlCapa) {

    public SessaoDTO(SessoesDeLeitura sessoesDeLeitura){

        this(
                sessoesDeLeitura.getUsuario().getId(),
                sessoesDeLeitura.getLivro().getId(),
                sessoesDeLeitura.getPaginaFinal() - sessoesDeLeitura.getPaginaInicial(), //PARA CALCULAR O TOTAL DE PÁGINAS LIDAS
                sessoesDeLeitura.getTempoLeitura(),
                sessoesDeLeitura.getPaginaFinal(),
                sessoesDeLeitura.getUrlCapa());
    }
}
