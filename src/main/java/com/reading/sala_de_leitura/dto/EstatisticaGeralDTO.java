package com.reading.sala_de_leitura.dto;

import java.util.List;

public record EstatisticaGeralDTO(
        List<LivroRankingDTO> rankingLivros,
        Long totalSegundosLidos,
        int totalPaginasLidas,
        int totalLivrosLidos
) { }
