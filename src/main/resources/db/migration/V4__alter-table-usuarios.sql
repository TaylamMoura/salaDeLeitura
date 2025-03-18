ALTER TABLE usuarios
    ADD email VARCHAR(150) NOT NULL UNIQUE,
    ADD senha_hash VARCHAR(255) NOT NULL,
    ADD data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
