package com.reading.sala_de_leitura.controller;


import com.reading.sala_de_leitura.dto.LivroDTO;
import com.reading.sala_de_leitura.entity.Livro;
import com.reading.sala_de_leitura.service.APIGoogleBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
public class LivroController {

    private APIGoogleBooksService service;

    @Autowired
    public  LivroController(APIGoogleBooksService service){
        this.service = service;
    }


    @GetMapping("/pesquisarLivro")
    public LivroDTO pesquisarLivro(@RequestParam  String titulo){
        return service.pesquisarPorTitulo(titulo);
    }

    @Transactional
    @PostMapping("/salvarLivro")
    public  String salvarLivro(@RequestBody LivroDTO livroDTO){
        Livro livro = new Livro(livroDTO.id(), livroDTO.titulo(), livroDTO.autor(), livroDTO.paginas(), livroDTO.urlCapa(), livroDTO.anoPublicacao());
        service.salvarLivro(livro);
        return "Livro salvo!";
    }

}
