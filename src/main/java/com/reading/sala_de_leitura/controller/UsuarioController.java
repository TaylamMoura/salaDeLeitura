package com.reading.sala_de_leitura.controller;

import com.reading.sala_de_leitura.dto.UsuarioCadastroDTO;
import com.reading.sala_de_leitura.dto.UsuarioLoginDTO;
import com.reading.sala_de_leitura.dto.UsuarioRetornoDTO;
import com.reading.sala_de_leitura.entity.Usuario;
import com.reading.sala_de_leitura.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> criarUsuario(@Valid @RequestBody UsuarioCadastroDTO cadastroDTO){
        Usuario novoUsuario = usuarioService.salvarUsuario(cadastroDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioRetornoDTO(novoUsuario));
    }


    @PostMapping("/login")
    public ResponseEntity<String> loginUsuario(@RequestBody UsuarioLoginDTO loginDTO){
        boolean credencialValida = usuarioService.verificarCredenciais(loginDTO.email(), loginDTO.senha());
        if (credencialValida){
            //retornar token depois
            return ResponseEntity.ok("Autenticação bem sucedida");
        }
        throw new IllegalArgumentException("Credencial Inválida!");
    }
}

//para colocar mensagens no status, usa-se <?> ao inves de <Usuario>