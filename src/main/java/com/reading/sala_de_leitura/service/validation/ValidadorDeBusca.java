//package com.reading.sala_de_leitura.service.validation;
//
//import com.reading.sala_de_leitura.dto.LivroDTO;
//import jakarta.validation.ValidationException;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ValidadorDeBusca implements Validador<LivroDTO>{
//
//    @Override
//    public void validar(LivroDTO dados) {
//        if (dados == null){
//            throw new ValidationException("Dados do livro são nulos");
//        }
//
//        var titulo = dados.titulo();
//        if (titulo == null || titulo.trim().isEmpty()){
//            throw new ValidationException("Titulo incorreto. O título não pode ser nulo ou vazio.");
//        }
//    }
//}

// este validador verificar se o objeto LivroDTO não é nulo
//e verifica se o titulo é nulo ou vazio.