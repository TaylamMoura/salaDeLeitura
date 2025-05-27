package com.reading.sala_de_leitura.dto;

import com.reading.sala_de_leitura.entity.Livro;

public record LivroDTO(
        Long id,
        String titulo,
        String autor,
        int paginas,
        String urlCapa,
        int anoPublicacao,
        int paginaAtual,
        boolean livroFinalizado) {

    public LivroDTO(Livro livro, int paginaAtual) {
        this(
            livro.getId(), 
            livro.getTitulo(), 
            livro.getAutor(), 
            livro.getPaginas(), 
            livro.getUrlCapa(), 
            livro.getAnoPublicacao(),
            paginaAtual,
            livro.getLivroFinalizado());
    }
    public LivroDTO(Livro livro){
        this(livro, 0);
    }
}
