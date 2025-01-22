CREATE TABLE sessoes(

    id BIGINT NOT NULL AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL,
    livro_id BIGINT NOT NULL,
    inicio_sessao TIME ,
    fim_sessao TIME,
    pagina_inicial INT NOT NULL,
    pagina_final INT NOT NULL,
    tempo_leitura TIME,
    url_capa VARCHAR(255) NOT NULL,

    PRIMARY KEY(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (livro_id) REFERENCES livros(id)

);