package com.reading.sala_de_leitura.dto;

import com.reading.sala_de_leitura.entity.Usuario;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.sql.Date;

public record UsuarioCadastroDTO(
        @NotEmpty
        String nome,

        @NotEmpty
        Date dataAniversario,

        @NotEmpty
        String usuario,

        @NotEmpty
        @Email
        String email,

        @NotEmpty
        @Size(min = 8, message = "A senha deve ter no mínimo 8 números")
        String senha
) {
    public UsuarioCadastroDTO(Usuario usuario){
        this(usuario.getNome(), usuario.getDataAniversario(), usuario.getUsuario(), usuario.getEmail(), usuario.getSenha());
    }
}
