package com.reading.sala_de_leitura.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.reading.sala_de_leitura.dto.LivroDTO;
import com.reading.sala_de_leitura.entity.Livro;
import com.reading.sala_de_leitura.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class APIGoogleBooksService {

    private final ConexaoAPI conexaoAPI;
    private final LivroRepository repository;

    @Autowired
    public APIGoogleBooksService(ConexaoAPI conexaoAPI, LivroRepository repository) {
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
            e.printStackTrace(); // Substituir por log robusto no futuro
            return null;
        }
    }

    public void salvarLivro(Livro livro){
        repository.save(livro);
    }
}

//Essa classe vai retornar um DTO para encapsular os dados basicos que serão exebidos
// para o usuario.
