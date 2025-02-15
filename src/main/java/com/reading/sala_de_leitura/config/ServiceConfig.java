package com.reading.sala_de_leitura.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ServiceConfig implements WebMvcConfigurer{
        @Override
        public void addCorsMappings(CorsRegistry registro){
            registro.addMapping("/**")
                    .allowedOrigins("http://localhost:8080")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT");

        }
}

