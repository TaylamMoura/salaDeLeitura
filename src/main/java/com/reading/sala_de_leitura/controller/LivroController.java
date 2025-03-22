package com.reading.sala_de_leitura.controller;


import com.reading.sala_de_leitura.dto.LivroDTO;
import com.reading.sala_de_leitura.entity.Livro;
import com.reading.sala_de_leitura.entity.Usuario;
import com.reading.sala_de_leitura.service.AtualizarLivro;
import com.reading.sala_de_leitura.service.LivroService;
import com.reading.sala_de_leitura.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LivroController {

    private LivroService service;
    private UsuarioService usuarioService;

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
    public ResponseEntity<LivroDTO> salvarLivro(@RequestBody LivroDTO livroDTO, Authentication authentication) {
        Usuario usuarioLogado = usuarioService.buscarPorEmail(authentication.getName());
        service.salvarLivro(livroDTO, usuarioLogado);
        return ResponseEntity.ok(livroDTO);
    }


    //Exibir na Página Inicial
    @GetMapping("/livrosSalvos")
    public List<LivroDTO> exibirLivros(Authentication authentication) {
        String email = authentication.getName();
        Usuario usarioLogado = usuarioService.buscarPorEmail(email);
        return service.exibirLivrosSalvos(usarioLogado);
    }


    @GetMapping("/exibirDados/{id}")
    public ResponseEntity<LivroDTO> exibirDados(@PathVariable Long id, Authentication authentication){
        Usuario usuarioLogado = usuarioService.buscarPorEmail(authentication.getName());
        LivroDTO livroDTO = service.exibirDadosLivro(id, usuarioLogado);
        return ResponseEntity.ok(livroDTO);

//        LivroDTO livroDTO = service.exibirDadosLivro(id);
//        if (livroDTO != null) {
//            return ResponseEntity.ok(livroDTO);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
    }

    @Transactional
    @DeleteMapping("/excluirLivro/{id}")
    public ResponseEntity<Void> excluirLivro(@PathVariable Long id, Authentication authentication) {
        Usuario usuarioLogado = usuarioService.buscarPorEmail(authentication.getName());
        service.deletarLivro(id, usuarioLogado);
        return ResponseEntity.ok().build();
    }


    @Transactional
    @PutMapping("/editarLivro/{id}")
    public ResponseEntity<LivroDTO> editarLivro(@PathVariable Long id, @RequestBody AtualizarLivro atualizarDados, Authentication authentication) {
        Usuario usuarioLogado = usuarioService.buscarPorEmail(authentication.getName());
        Livro livroAtualizado = service.editarLivro(id, atualizarDados, usuarioLogado);
        LivroDTO livroDTO = new LivroDTO(
                livroAtualizado.getId(),
                livroAtualizado.getTitulo(),
                livroAtualizado.getAutor(),
                livroAtualizado.getPaginas(),
                livroAtualizado.getUrlCapa(),
                livroAtualizado.getAnoPublicacao()
        );
        return ResponseEntity.ok(livroDTO);
    }

}


