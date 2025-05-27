package com.reading.sala_de_leitura.controller;

import com.reading.sala_de_leitura.dto.EstatisticaGeralDTO;
import com.reading.sala_de_leitura.dto.EstatisticaLivroDTO;
import com.reading.sala_de_leitura.entity.Usuario;
import com.reading.sala_de_leitura.service.EstatisticaService;
import com.reading.sala_de_leitura.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;

@RestController
public class EstatisticasController {

    private final EstatisticaService estatisticaService;
    private final UsuarioService usuarioService;

    @Autowired
    public EstatisticasController(EstatisticaService estatisticaService, UsuarioService usuarioService){
        this.estatisticaService = estatisticaService;
        this.usuarioService = usuarioService;
    }


    //MÉTODO PARA BUSCAR USUÁRIO LOGADO
    private Usuario usuarioLogado(Authentication authentication){
        return usuarioService.buscarPorEmail(authentication.getName());
    }


    //ESTATÍSTICA DO LIVRO DO USUÁRIO
    @GetMapping("/estatistica-livro")
    public ResponseEntity<EstatisticaLivroDTO> estatisticaLivro (@RequestParam("livroId") Long livroId, Authentication authentication){
        return ResponseEntity.ok(estatisticaService.estatisticasLivro(livroId, usuarioLogado(authentication)));
    }


    //ESTATÍSTICA GERAL DO USUÁRIO
    @GetMapping("/estatistica-geral")
    public ResponseEntity<EstatisticaGeralDTO> estatisticaGeral (Authentication authentication) {
        try {
            EstatisticaGeralDTO estatisticas = estatisticaService.estatisticaGeral(usuarioLogado(authentication));
            return ResponseEntity.ok(estatisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EstatisticaGeralDTO(Collections.emptyList(), 0L, 0, 0));
        }
    }
}
