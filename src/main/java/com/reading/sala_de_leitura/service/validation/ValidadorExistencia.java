package com.reading.sala_de_leitura.service.validation;

import com.reading.sala_de_leitura.dto.LivroDTO;

public interface ValidadorExistencia <T> {
    boolean validar(T dados);
}
