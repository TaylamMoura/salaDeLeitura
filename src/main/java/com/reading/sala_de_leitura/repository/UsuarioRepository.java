package com.reading.sala_de_leitura.repository;

import com.reading.sala_de_leitura.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsuario(String usuario);

    Optional<Usuario> findByEmail(String email);
}
