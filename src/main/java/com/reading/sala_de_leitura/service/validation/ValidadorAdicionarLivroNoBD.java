package com.reading.sala_de_leitura.service.validation;

import com.reading.sala_de_leitura.dto.LivroDTO;
import com.reading.sala_de_leitura.entity.Livro;
import com.reading.sala_de_leitura.repository.LivroRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorAdicionarLivroNoBD implements ValidadorExistencia<LivroDTO> {

    @Autowired
    private LivroRepository repository;


    @Override
    public boolean validar(LivroDTO livroDTO) {

        if (livroDTO == null) {
            throw new ValidationException("Dados do livro são nulos");
            //MUDAR A MSG DA EXCEPTION DEPOIS
        }

        if ( repository.existsById(livroDTO.id())) {
            throw new ValidationException("Livro nao foi salvo no banco de dador");
        }
        return true;

    }
}

//este validador verifica se LivroDTO nao é nulo
// e verifica se o livro foi add no BD.

