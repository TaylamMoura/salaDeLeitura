package com.reading.sala_de_leitura.controller;

import com.reading.sala_de_leitura.dto.UsuarioCadastroDTO;
import com.reading.sala_de_leitura.dto.UsuarioLoginDTO;
import com.reading.sala_de_leitura.dto.UsuarioRetornoDTO;
import com.reading.sala_de_leitura.entity.Usuario;
import com.reading.sala_de_leitura.security.JwtService;
import com.reading.sala_de_leitura.service.UsuarioService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;


    @PostMapping
    public ResponseEntity<?> criarUsuario(@Valid @RequestBody UsuarioCadastroDTO cadastroDTO){
        Usuario novoUsuario = usuarioService.salvarUsuario(cadastroDTO);
        System.out.println("DTO recebido: " + cadastroDTO); //REMOVER
            return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioRetornoDTO(novoUsuario));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUsuario(
            @RequestBody UsuarioLoginDTO loginDTO,
            HttpServletResponse response) {
        try {
            boolean credencialValida = usuarioService.verificarCredenciais(loginDTO.email(), loginDTO.senha());

            if (credencialValida) {
                String token = jwtService.generateToken(loginDTO.email());

                //CRIAÇÃO DO COOKIE COM O TOKEN
                ResponseCookie cookie = ResponseCookie.from("jwt", token)
                        .httpOnly(true)
                        .secure(false)
                        .path("/")
                        .maxAge(3600)
                        .build();
                response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

                Map<String, String> responseBody = new HashMap<>();
                responseBody.put("mensagem", "Autenticação bem-sucedida");
                return ResponseEntity.ok(responseBody);

            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("mensagem", "Credenciais inválidas"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensagem", "Erro interno no servidor"));
        }
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            Claims claims = jwtService.validateToken(token.replace("Bearer ", ""));
            String email = claims.getSubject();
            return ResponseEntity.ok("Token válido para o email: " + email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou expirado");
        }
    }
}

//para colocar mensagens no status, usa-se <?> ao inves de <Usuario>