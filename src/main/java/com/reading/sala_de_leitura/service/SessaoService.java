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

import java.sql.Time;

@Service
public class SessaoService {

    private final SessoesRepository sessoesRepository;
    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;
    private Time inicioSessao;

    //Instância da entidade SessaoService
    @Autowired
    public SessaoService(SessoesRepository sessoesRepository, UsuarioRepository usuarioRepository, LivroRepository livroRepository) {
        this.sessoesRepository = sessoesRepository;
        this.usuarioRepository = usuarioRepository;
        this.livroRepository = livroRepository;
    }

    // Funções do Cronômetro
   public void iniciarSessao() {
        inicioSessao = new Time(System.currentTimeMillis());
   }

    // Finalizar Sessão
    public SessoesDeLeitura finalizarSessao(Long livroId,  int paginasLidas, String tempoLeituraString) {
        Time tempoLeitura = Time.valueOf(tempoLeituraString);

        // Recuperar o email do usuário autenticado
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuarioLogado = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // Buscar o livro
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));

        // Validação: Verificar se paginasLidas não excede o total de páginas do livro
        if (paginasLidas > livro.getPaginas()) {
            throw new IllegalArgumentException("Número de páginas lidas não pode ser maior que o total de páginas do livro.");
        }

        // Criar a instância de SessoesDeLeitura
        SessoesDeLeitura sessao = new SessoesDeLeitura();
        sessao.setUsuario(usuarioLogado);
        sessao.setLivro(livro);
        sessao.setInicioSessao(inicioSessao);
        sessao.setFimSessao(new Time(System.currentTimeMillis())); // Horário atual

        int ultimaPagina = sessoesRepository
                    .findTopByUsuarioIdAndLivroIdOrderByFimSessaoDesc(usuarioLogado.getId(), livroId)
                    .map(SessoesDeLeitura::getPaginaFinal).orElse(0);
        sessao.setPaginaInicial(ultimaPagina);

        sessao.setPaginaFinal(paginasLidas);
        sessao.setTempoLeitura(tempoLeitura);
        sessao.setUrlCapa(livro.getUrlCapa());

        // Salvar no repositório e retornar
        return sessoesRepository.save(sessao);
    }

    public int buscarUltimaPagina(Long livroId) {
        return sessoesRepository.findTopByLivroIdOrderByFimSessaoDesc(livroId)
                    .map(SessoesDeLeitura::getPaginaFinal).orElse(0);
    }
}