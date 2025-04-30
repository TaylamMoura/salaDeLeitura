package com.reading.sala_de_leitura.controller;


import com.reading.sala_de_leitura.dto.LivroDTO;
import com.reading.sala_de_leitura.dto.UsuarioRetornoDTO;
import com.reading.sala_de_leitura.entity.Livro;
import com.reading.sala_de_leitura.entity.SessoesDeLeitura;
import com.reading.sala_de_leitura.entity.Usuario;
import com.reading.sala_de_leitura.repository.LivroRepository;
import com.reading.sala_de_leitura.repository.SessoesRepository;
import com.reading.sala_de_leitura.service.AtualizarLivro;
import com.reading.sala_de_leitura.service.LivroService;
import com.reading.sala_de_leitura.service.UsuarioService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LivroController {

    private final LivroService livroService;
    private final UsuarioService usuarioService;
    private final SessoesRepository sessoesRepository;
    private final LivroRepository livroRepository;

    @Autowired
    public LivroController(LivroService service, UsuarioService usuarioService, SessoesRepository sessoesRepository, LivroRepository livroRepository) {
        this.livroService = service;
        this.usuarioService = usuarioService;
        this.sessoesRepository = sessoesRepository;
        this.livroRepository = livroRepository;
    }


    @GetMapping("/pesquisarLivro")
    public LivroDTO pesquisarLivro(@RequestParam String titulo) {
        LivroDTO livroDTO = new LivroDTO(null, titulo, null, 0, null, 0, 0);
        return livroService.pesquisarPorTitulo(titulo);
    }


    @Transactional
    @PostMapping("/salvarLivro")
    public ResponseEntity<LivroDTO> salvarLivro(@RequestBody LivroDTO livroDTO, Authentication authentication) {

        String emailLogado = authentication.getName();
        Usuario usuarioLogado = usuarioService.buscarPorEmail(emailLogado);
        livroService.salvarLivro(livroDTO, usuarioLogado);
        return ResponseEntity.ok(livroDTO);
    }


    //Exibir na Página Inicial
    @GetMapping("/livrosSalvos")
    public List<LivroDTO> exibirLivros(Authentication authentication) {
        String email = authentication.getName();
        Usuario usarioLogado = usuarioService.buscarPorEmail(email);
        return livroService.exibirLivrosSalvos(usarioLogado);
    }


    @GetMapping("/exibirDados/{id}")
    public ResponseEntity<LivroDTO> exibirDados(@PathVariable Long id, Authentication authentication){
        Usuario usuarioLogado = usuarioService.buscarPorEmail(authentication.getName());
        //LivroDTO livroDTO = livroService.exibirDadosLivro(id, usuarioLogado);

        Livro livro = livroRepository.findByIdAndUsuario(id, usuarioLogado)
                .orElseThrow(() -> new ValidationException("Livro não encontrado"));

        int paginaAtual = sessoesRepository.findTopByUsuarioIdAndLivroIdOrderByFimSessaoDesc(usuarioLogado.getId(), livro.getId())
                            .map(SessoesDeLeitura::getPaginaFinal)
                            .orElse(0);

        LivroDTO livroDTO = new LivroDTO(livro, paginaAtual);

        return ResponseEntity.ok(livroDTO);
    }

    @Transactional
    @DeleteMapping("/excluirLivro/{id}")
    public ResponseEntity<Void> excluirLivro(@PathVariable Long id, Authentication authentication) {
        Usuario usuarioLogado = usuarioService.buscarPorEmail(authentication.getName());
        livroService.deletarLivro(id, usuarioLogado);
        return ResponseEntity.ok().build();
    }


    @Transactional
    @PutMapping("/editarLivro/{id}")
    public ResponseEntity<LivroDTO> editarLivro(@PathVariable Long id, @RequestBody AtualizarLivro atualizarDados, Authentication authentication) {
        Usuario usuarioLogado = usuarioService.buscarPorEmail(authentication.getName());
        Livro livroAtualizado = livroService.editarLivro(id, atualizarDados, usuarioLogado);
        LivroDTO livroDTO = new LivroDTO(
                livroAtualizado.getId(),
                livroAtualizado.getTitulo(),
                livroAtualizado.getAutor(),
                livroAtualizado.getPaginas(),
                livroAtualizado.getUrlCapa(),
                livroAtualizado.getAnoPublicacao(),
                0 //Valor default
        );
        return ResponseEntity.ok(livroDTO);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        SecurityContextHolder.clearContext();

        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .domain("localhost")
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok("Logout feito");
    }

    //exibir nome de usuario no front
    @GetMapping("/usuario-logado")
    public ResponseEntity<UsuarioRetornoDTO> obterUsuarioLogado(Authentication authentication) {
        try {
            String emailLogado = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(emailLogado);

            return ResponseEntity.ok(new UsuarioRetornoDTO(usuario));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
