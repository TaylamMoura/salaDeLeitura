CREATE TABLE livros(

    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    paginas INT,
    url_capa VARCHAR(255) NOT NULL,
    ano_publicacao INT,

    PRIMARY KEY(id)
);