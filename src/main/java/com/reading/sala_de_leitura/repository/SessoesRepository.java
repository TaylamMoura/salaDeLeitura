package com.reading.sala_de_leitura.repository;

import com.reading.sala_de_leitura.entity.Livro;
import com.reading.sala_de_leitura.entity.SessoesDeLeitura;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface SessoesRepository extends JpaRepository<SessoesDeLeitura, Long> {

    Optional<SessoesDeLeitura> findTopByUsuarioIdAndLivroIdOrderByFimSessaoDesc(Long usuarioId, Long livroId);

    Optional<SessoesDeLeitura> findTopByLivroIdOrderByFimSessaoDesc(Long livroId);

}
