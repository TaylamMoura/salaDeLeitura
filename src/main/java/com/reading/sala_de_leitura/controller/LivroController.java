package com.reading.sala_de_leitura.controller;


import com.reading.sala_de_leitura.dto.LivroDTO;
import com.reading.sala_de_leitura.service.LivroService;
import com.reading.sala_de_leitura.service.validation.Validador;
import com.reading.sala_de_leitura.service.validation.ValidadorExistencia;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
public class LivroController {

    private LivroService service;

    @Autowired
    private ValidadorExistencia<LivroDTO> validadorAdicionarNoBD;

    @Autowired
    private Validador<LivroDTO> validadorDeBusca;

    @Autowired
    public  LivroController(LivroService service){
        this.service = service;
    }


    @GetMapping("/pesquisarLivro")
    public LivroDTO pesquisarLivro(@RequestParam  String titulo){
        LivroDTO livroDTO = new LivroDTO(null, titulo, null, 0, null, 0);
        validadorDeBusca.validar(livroDTO);
        return service.pesquisarPorTitulo(titulo);
    }


    @Transactional
    @PostMapping("/salvarLivro")
    public  String salvarLivro(@RequestBody LivroDTO livroDTO) {
        service.salvarLivro(livroDTO);
        return "Livro salvo com sucesso";
    }

}
