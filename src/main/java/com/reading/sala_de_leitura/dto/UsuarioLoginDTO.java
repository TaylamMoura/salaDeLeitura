package com.reading.sala_de_leitura.dto;

import com.reading.sala_de_leitura.entity.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UsuarioLoginDTO(
        @NotEmpty(message = "O email é obrigatório!")
        @Email(message = "Escreva um email válido!")
        String email,

        @NotEmpty(message = "Digite sua senha.")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 números")
        String senha) {

    public UsuarioLoginDTO(Usuario usuario){
        this(usuario.getEmail(), usuario.getSenha());
    }
}
