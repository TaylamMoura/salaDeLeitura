package com.reading.sala_de_leitura.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.reading.sala_de_leitura.dto.LivroDTO;
import com.reading.sala_de_leitura.entity.Livro;
import com.reading.sala_de_leitura.entity.Usuario;
import com.reading.sala_de_leitura.repository.LivroRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class LivroService {

    private final ConexaoAPI conexaoAPI;
    private final LivroRepository repository;

    @Autowired
    public LivroService(ConexaoAPI conexaoAPI, LivroRepository repository) {
        this.conexaoAPI = conexaoAPI;
        this.repository = repository;
    }

    public LivroDTO pesquisarPorTitulo(String titulo) {
        try {
            String tituloEncoded = URLEncoder.encode("intitle:" + titulo, StandardCharsets.UTF_8.toString());
            String jsonResponse = conexaoAPI.livrosJson(tituloEncoded);

            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            JsonArray items = jsonObject.getAsJsonArray("items");

            if (items != null && !items.isEmpty()) {
                JsonObject volumeInfo = items.get(0).getAsJsonObject().getAsJsonObject("volumeInfo");
                String title = volumeInfo.get("title").getAsString();
                String author = volumeInfo.getAsJsonArray("authors").get(0).getAsString();
                int pageCount = volumeInfo.get("pageCount").getAsInt();
                String thumbnail = volumeInfo.getAsJsonObject("imageLinks").get("thumbnail").getAsString();
                String publishedDate = volumeInfo.get("publishedDate").getAsString();

                return new LivroDTO(null, title, author, pageCount, thumbnail, publishedDate != null ? Integer.parseInt(publishedDate.split("-")[0]) : 0);

            } else {
                System.out.println("Livro não encontrado. Tente outro título!");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Transactional
    public void salvarLivro(LivroDTO livroDTO, Usuario usuarioLogado){

        String urlCapa = livroDTO.urlCapa();

        if (urlCapa == null || urlCapa.trim().isEmpty()){
            urlCapa = "https://via.placeholder.com/150";
        }

        Livro livro =new Livro(
                null,
                livroDTO.titulo(),
                livroDTO.autor(),
                livroDTO.paginas(),
                urlCapa,
                livroDTO.anoPublicacao(),
                usuarioLogado
        );

        repository.save(livro);
    }


    public List<LivroDTO> exibirLivrosSalvos(Usuario usuario){
        List<Livro> livros = repository.findByUsuario(usuario);
        return livros.stream().map(LivroDTO::new).toList();
    }


    @Transactional
    public void deletarLivro(Long id, Usuario usuario){
        Livro livro = repository.findById(id)
                .orElseThrow(() -> new ValidationException("Livro não escontrado"));

        if (!livro.getUsuario().equals(usuario)){
            throw new ValidationException("Você não tem permissão para excluir este livro.");
        }
        repository.delete(livro);
    }


    @Transactional
    public Livro editarLivro(Long id, AtualizarLivro atualizarDados, Usuario usuario) {
        Livro livro = repository.findById(id)
                .orElseThrow(() -> new ValidationException("Livro não escontrado"));
        if (!livro.getUsuario().equals(usuario)){
            throw new ValidationException("Você não tem permissão para excluir este livro.");
        }
        livro.atualizar(atualizarDados);
        repository.save(livro);
        return livro;
    }


    public LivroDTO exibirDadosLivro(Long id, Usuario usuario) {
        Livro livro = repository.findByIdAndUsuario(id, usuario)
                .orElseThrow(()-> new ValidationException("Livro não encontrado"));
        return  new LivroDTO(livro);
    }
}
