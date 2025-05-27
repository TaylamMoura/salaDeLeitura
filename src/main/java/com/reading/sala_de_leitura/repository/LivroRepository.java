package com.reading.sala_de_leitura.repository;

import com.reading.sala_de_leitura.entity.Livro;
import com.reading.sala_de_leitura.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findByUsuario(Usuario usuario);

    Optional<Livro> findByIdAndUsuario(Long id, Usuario usuario);

    List<Livro> findByUsuarioAndLivroFinalizadoTrue(Usuario usuario);

}
