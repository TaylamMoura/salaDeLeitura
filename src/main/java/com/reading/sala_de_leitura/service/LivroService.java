package com.reading.sala_de_leitura.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.reading.sala_de_leitura.dto.LivroDTO;
import com.reading.sala_de_leitura.entity.Livro;
import com.reading.sala_de_leitura.repository.LivroRepository;
import com.reading.sala_de_leitura.service.validation.ValidadorExistencia;
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
    private ValidadorExistencia<LivroDTO> validadorAdicionarNoBD;

    @Autowired
    public LivroService(ConexaoAPI conexaoAPI, LivroRepository repository) {
        this.conexaoAPI = conexaoAPI;
        this.repository = repository;
    }

    public LivroDTO pesquisarPorTitulo(String titulo) {
        try {
            String tituloEncoded = URLEncoder.encode("intitle:" + titulo, StandardCharsets.UTF_8.toString());
            String jsonResponse = conexaoAPI.livrosJson(tituloEncoded);

            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject(); //analizar e converte um objeto em json
            JsonArray items = jsonObject.getAsJsonArray("items");

            if (items != null && !items.isEmpty()) {
                JsonObject volumeInfo = items.get(0).getAsJsonObject().getAsJsonObject("volumeInfo"); // converte o item em um jsonObject
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
            e.printStackTrace(); // MUDAR AQUI POR ALGO MELHOR
            return null;
        }
    }

    @Transactional
    public void salvarLivro(LivroDTO livroDTO){
        validadorAdicionarNoBD.validar(livroDTO);

        Livro livro =new Livro(null, livroDTO.titulo(), livroDTO.autor(), livroDTO.paginas(), livroDTO.urlCapa(), livroDTO.anoPublicacao());
        repository.save(livro);
    }

    public List<LivroDTO> exibirLivrosSalvos(){
        List<Livro> livros = repository.findAll();
        return livros.stream().map(livro -> new LivroDTO(livro)).toList();
    }
}

//Essa classe vai retornar um DTO para encapsular os dados basicos que serão exebidos
// para o usuario.
