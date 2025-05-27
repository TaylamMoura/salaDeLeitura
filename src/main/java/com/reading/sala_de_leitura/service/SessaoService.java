package com.reading.sala_de_leitura.service;

import com.reading.sala_de_leitura.entity.Livro;
import com.reading.sala_de_leitura.entity.SessoesDeLeitura;
import com.reading.sala_de_leitura.entity.Usuario;
import com.reading.sala_de_leitura.repository.LivroRepository;
import com.reading.sala_de_leitura.repository.SessoesRepository;
import com.reading.sala_de_leitura.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SessaoService {

    private final SessoesRepository sessoesRepository;
    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;


    //Instância da entidade SessaoService
    @Autowired
    public SessaoService(SessoesRepository sessoesRepository, UsuarioRepository usuarioRepository, LivroRepository livroRepository) {
        this.sessoesRepository = sessoesRepository;
        this.usuarioRepository = usuarioRepository;
        this.livroRepository = livroRepository;
    }

    // Funções do Cronômetro
   public SessoesDeLeitura iniciarSessao(Long livroId) {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    Usuario usuarioLogado = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

    // Buscar o livro
    Livro livro = livroRepository.findById(livroId)
            .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));

    SessoesDeLeitura tempoLeitura = new SessoesDeLeitura();
    tempoLeitura.setUsuario(usuarioLogado);
    tempoLeitura.setLivro(livro);
    tempoLeitura.setInicioSessao(LocalDateTime.now());

    return sessoesRepository.save(tempoLeitura);
}


    // Finalizar Sessão
    @Transactional
    public SessoesDeLeitura finalizarSessao(Long livroId, int paginasLidas, int tempoLeituraSegundos) {

        // Recuperar usuário autenticado
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuarioLogado = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // Buscar livro
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));

        // Busca a última página registrada para definir 'pagina_inicial'
        int ultimaPagina = sessoesRepository.findTopByUsuarioIdAndLivroIdOrderByFimSessaoDesc(usuarioLogado.getId(), livroId)
                .map(SessoesDeLeitura::getPaginaFinal)
                .orElse(0);

        // Cria uma nova sessão
        SessoesDeLeitura novaSessao = new SessoesDeLeitura();
        novaSessao.setUsuario(usuarioLogado);
        novaSessao.setLivro(livro);
        novaSessao.setInicioSessao(LocalDateTime.now());
        novaSessao.setFimSessao(novaSessao.getInicioSessao().plusSeconds(tempoLeituraSegundos));
        novaSessao.setTempoLeitura(tempoLeituraSegundos);
        novaSessao.setPaginaInicial(ultimaPagina);
        novaSessao.setPaginaFinal(paginasLidas);

        // Verifica se o usuário atingiu a última página do livro e marca como finalizado
        if (paginasLidas == livro.getPaginas()) {
            livro.setLivroFinalizado(true);
            livroRepository.save(livro);
        }
        return sessoesRepository.save(novaSessao);
}

    public int buscarUltimaPagina(Long livroId) {
        return sessoesRepository.findTopByLivroIdOrderByFimSessaoDesc(livroId)
                    .map(SessoesDeLeitura::getPaginaFinal).orElse(0);
    }
}
