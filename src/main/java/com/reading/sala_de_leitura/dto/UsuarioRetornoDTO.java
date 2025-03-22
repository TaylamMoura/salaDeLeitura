package com.reading.sala_de_leitura.dto;

import com.reading.sala_de_leitura.entity.Usuario;

public record UsuarioRetornoDTO(
        String usuario,
        String email) {

    public UsuarioRetornoDTO(Usuario usuario){
        this(usuario.getUsuario(), usuario.getEmail());
    }
}
