package com.reading.sala_de_leitura.repository;

import com.reading.sala_de_leitura.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository extends JpaRepository<Livro, Long> {
}
