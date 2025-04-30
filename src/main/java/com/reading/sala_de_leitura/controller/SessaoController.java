package com.reading.sala_de_leitura.controller;

import com.reading.sala_de_leitura.dto.SessaoDTO;
import com.reading.sala_de_leitura.entity.SessoesDeLeitura;
import com.reading.sala_de_leitura.service.SessaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessao-leitura")
public class SessaoController {

    private final SessaoService sessaoService;

    public  SessaoController(SessaoService sessaoService){
        this.sessaoService = sessaoService;
    }


    @PostMapping("/iniciar")
    public ResponseEntity<Void> iniciarSessao(@Valid @RequestBody SessaoDTO sessaoDTO) {
        sessaoService.iniciarSessao();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/finalizar")
    public ResponseEntity<SessaoDTO> finalizarSessao(@RequestBody SessaoDTO sessaoDTO){
        
        SessoesDeLeitura sessao = sessaoService.finalizarSessao(
                sessaoDTO.livroId(),
                sessaoDTO.paginasLidas(),
                sessaoDTO.tempoLeitura()
        );
        SessaoDTO dto = new SessaoDTO(sessao);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    //ENDPOINT PARA VERIFICAÇÃO DA ULTIMA PÁGINA LIDA
    @GetMapping("/ultima-pagina/{id}")
    public ResponseEntity<Integer> paginaAtual(@PathVariable Long id){
        try{
            int paginaFinal = sessaoService.buscarUltimaPagina(id);
            return ResponseEntity.ok(paginaFinal);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } 
    }
}
