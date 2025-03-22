package com.reading.sala_de_leitura.service;

import com.reading.sala_de_leitura.dto.UsuarioCadastroDTO;
import com.reading.sala_de_leitura.entity.Usuario;
import com.reading.sala_de_leitura.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //CRIAR CADASTRO DE USUARIO
    @Transactional
    public Usuario salvarUsuario(UsuarioCadastroDTO cadastroDTO){
        Usuario usuario = new Usuario();
        usuario.setNome(cadastroDTO.nome());
        usuario.setDataAniversario(cadastroDTO.dataAniversario());
        usuario.setEmail(cadastroDTO.email());
        usuario.setUsuario(cadastroDTO.usuario());
        usuario.setSenha(passwordEncoder.encode(cadastroDTO.senha()));

        return usuarioRepository.save(usuario);
    }


    //verificar usuário e senha
    public boolean verificarCredenciais(String email, String senha) {
        return usuarioRepository.findByEmail(email)
                .map(usuario -> passwordEncoder.matches(senha, usuario.getSenha()))
                .orElse(false);
    }

    //BUSCA USUARIO PELO EMAIL
    public Usuario buscarPorEmail(String email){
        return usuarioRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("Usuário com o e-mail " + email + " não foi encontrado."));
    }
}

//Bloqueio após tentativas falhas: Impedir ataques de força bruta limitando o número de tentativas de login.