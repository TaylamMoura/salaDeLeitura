package com.reading.sala_de_leitura.dto;

public record LivroRankingDTO(
    Long id,
    String titulo,
    String autor,
    String urlCapa) {
}