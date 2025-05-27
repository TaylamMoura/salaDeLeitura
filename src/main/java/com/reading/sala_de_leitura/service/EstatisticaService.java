package com.reading.sala_de_leitura.service;

import com.reading.sala_de_leitura.dto.EstatisticaGeralDTO;
import com.reading.sala_de_leitura.dto.EstatisticaLivroDTO;
import com.reading.sala_de_leitura.dto.LivroRankingDTO;
import com.reading.sala_de_leitura.entity.Usuario;
import com.reading.sala_de_leitura.repository.EstatisticaRepository;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class EstatisticaService {

    private EstatisticaRepository estatisticaRepository;


    public EstatisticaService(EstatisticaRepository estatisticaRepository) {
        this.estatisticaRepository = estatisticaRepository;
    }


    //Service de Estatistíca do LIVRO
    public EstatisticaLivroDTO estatisticasLivro(Long livroId, Usuario usuarioLogado) {
        return new EstatisticaLivroDTO(
            estatisticaRepository.calcularDiasParaTerminarLivro(livroId).orElse(0),
            estatisticaRepository.calcularMediaPaginasPorDia(livroId).orElse(0.0),
            estatisticaRepository.calcularMediaTempoSessao(livroId).orElse(0.0)
        );
    }


    //Service de Estatistíca GERAL
    public EstatisticaGeralDTO estatisticaGeral(Usuario usuarioLogado) {
        Long usuarioId = usuarioLogado.getId();
        List<LivroRankingDTO> rankingLivros = estatisticaRepository.rankingLivros(usuarioLogado.getId());

        // Converte a string "HH:MM:SS" para segundos usando nosso método auxiliar (converterHorasParaSegundos)
        Long totalSegundosLidos = Optional.ofNullable(estatisticaRepository.totalSegundosLidos())
        .orElse(0L);

        return new EstatisticaGeralDTO(
                rankingLivros,
                totalSegundosLidos,
                estatisticaRepository.totalPaginasLidas(),
                estatisticaRepository.totalLivrosLidos()
        );
    }


    // Método auxiliar para converter "HH:MM:SS" em segundos
    private long converterHorasParaSegundos(String horasFormatadas) {
        if (horasFormatadas == null || horasFormatadas.isEmpty()) {
            return 0L;
        }
        LocalTime tempo = LocalTime.parse(horasFormatadas);
        return tempo.toSecondOfDay();
    }


    private String formatarHorasMinutos(Long segundos) {
        if (segundos == null) return "0 horas e 0 minutos";

        long horas = segundos / 3600;
        long minutos = (segundos % 3600) / 60;
        return horas + " horas e " + minutos + " minutos";
    }

}
