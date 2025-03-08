package com.reading.sala_de_leitura.controller;


import com.reading.sala_de_leitura.dto.LivroDTO;
import com.reading.sala_de_leitura.entity.Livro;
import com.reading.sala_de_leitura.service.AtualizarLivro;
import com.reading.sala_de_leitura.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LivroController {

    private LivroService service;

    @Autowired
    public LivroController(LivroService service) {
        this.service = service;
    }


    @GetMapping("/pesquisarLivro")
    public LivroDTO pesquisarLivro(@RequestParam String titulo) {
        LivroDTO livroDTO = new LivroDTO(null, titulo, null, 0, null, 0);
        return service.pesquisarPorTitulo(titulo);
    }


    @Transactional
    @PostMapping("/salvarLivro")
    public ResponseEntity<LivroDTO> salvarLivro(@RequestBody LivroDTO livroDTO) {
        try {
            service.salvarLivro(livroDTO);
            return ResponseEntity.ok(livroDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //Exibir na Página Inicial
    @GetMapping("/livrosSalvos")
    public List<LivroDTO> exibirLivros() {
        return service.exibirLivrosSalvos();
    }

    @GetMapping("/exibirDados/{id}")
    public ResponseEntity<LivroDTO> exibirDados(@PathVariable Long id){
        LivroDTO livroDTO = service.exibirDadosLivro(id);
        if (livroDTO != null) {
            return ResponseEntity.ok(livroDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    @DeleteMapping("/excluirLivro/{id}")
    public ResponseEntity<Void> excluirLivro(@PathVariable Long id) {
        try {
            service.deletarLivro(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional
    @PutMapping("/editarLivro/{id}")
    public ResponseEntity<LivroDTO> editarLivro(@PathVariable Long id, @RequestBody AtualizarLivro atualizarDados){
        try{
            Livro livroAtualizado = service.editarLivro(id, atualizarDados);
            LivroDTO livroDTO = new LivroDTO(
                    livroAtualizado.getId(),
                    livroAtualizado.getTitulo(),
                    livroAtualizado.getAutor(),
                    livroAtualizado.getPaginas(),
                    livroAtualizado.getUrlCapa(),
                    livroAtualizado.getAnoPublicacao()
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


