package com.reading.sala_de_leitura.repository;

import com.reading.sala_de_leitura.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
