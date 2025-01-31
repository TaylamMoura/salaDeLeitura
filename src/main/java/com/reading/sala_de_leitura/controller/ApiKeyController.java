package com.reading.sala_de_leitura.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiKeyController {

    @Value("${google.api.key}")
    private String apikey;

    @GetMapping("/api-key")
    public  String getApikey(){
        return apikey;
    }

}
