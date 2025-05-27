package com.reading.sala_de_leitura.service;

import com.reading.sala_de_leitura.dto.UsuarioCadastroDTO;
import com.reading.sala_de_leitura.entity.Usuario;
import com.reading.sala_de_leitura.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String gerarToken(String email) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora de validade
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    @Transactional
    public Usuario salvarUsuario(UsuarioCadastroDTO cadastroDTO) {
        Usuario usuario = new Usuario();
        usuario.setNome(cadastroDTO.nome());
        usuario.setDataNascimento(cadastroDTO.dataNascimento());
        usuario.setEmail(cadastroDTO.email());
        usuario.setUsuario(cadastroDTO.usuario());
        usuario.setSenha(passwordEncoder.encode(cadastroDTO.senha()));

        return usuarioRepository.save(usuario);
    }

    public boolean verificarCredenciais(String email, String senha) {
        return usuarioRepository.findByEmail(email)
                .map(usuario -> passwordEncoder.matches(senha, usuario.getSenha()))
                .orElse(false);
    }


    public Usuario buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário com o e-mail " + email + " não foi encontrado."));
        return usuario;
    }
}
