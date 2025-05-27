package com.reading.sala_de_leitura.repository;

import com.reading.sala_de_leitura.dto.LivroRankingDTO;
import com.reading.sala_de_leitura.entity.SessoesDeLeitura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface EstatisticaRepository extends JpaRepository<SessoesDeLeitura, Long> {

    // Calcula quantos dias levou para finalizar o livro
    @Query(value = "SELECT COALESCE(TIMESTAMPDIFF(DAY, MIN(s.inicio_sessao), MAX(s.fim_sessao)), 0) FROM sessoes s WHERE s.livro_id = :livroId", nativeQuery = true)
    Optional<Integer> calcularDiasParaTerminarLivro(@Param("livroId") Long livroId);


    // Calcula a média de páginas lidas por dia, evitando divisão por zero
    @Query(value = "SELECT COALESCE(SUM(s.pagina_final - s.pagina_inicial) / NULLIF(TIMESTAMPDIFF(DAY, MIN(s.inicio_sessao), MAX(s.fim_sessao)), 0), 0) FROM sessoes s WHERE s.livro_id = :livroId", nativeQuery = true)
    Optional<Double> calcularMediaPaginasPorDia(@Param("livroId") Long livroId);


    // Calcula a média de tempo das sessões de leitura
    @Query(value = "SELECT COALESCE(AVG(s.tempo_leitura) / 3600, 0) FROM sessoes s WHERE s.livro_id = :livroId", nativeQuery = true)
    Optional<Double> calcularMediaTempoSessao(@Param("livroId") Long livroId);


    //Ranking com os 5 livros finalizados, definidos de modo aleatório
    @Query(value = "SELECT l.id, l.titulo, l.autor, l.url_capa " +
            "FROM livros l " +
            "JOIN sessoes s ON s.livro_id = l.id " +
            "WHERE s.usuario_id = :usuarioId " +
            "AND s.finalizado = TRUE " +
            "GROUP BY l.id, l.titulo, l.autor, l.url_capa " +
            "ORDER BY RAND() " +
            "LIMIT 5", 
            nativeQuery = true)
    List<LivroRankingDTO> rankingLivros(@Param("usuarioId") Long usuarioId);


    //Retorna os segundos para ser convertido em Total de horas lidas no service -- evita erros de conversão no SQL
    @Query(value = "SELECT SUM(tempo_leitura) FROM sessoes", nativeQuery = true)
    Long totalSegundosLidos();


    // Total de PÁGINAS lidas em todas as sessões
    @Query("SELECT SUM(s.paginaFinal - s.paginaInicial) FROM SessoesDeLeitura s")
    int totalPaginasLidas();


    //Total de livros lidos
    @Query("SELECT COUNT(*) FROM Livro l WHERE l.livroFinalizado = TRUE")
    int totalLivrosLidos();
}

