ALTER TABLE livros ADD COLUMN usuario_id BIGINT;

ALTER TABLE livros
ADD CONSTRAINT fk_usuario_livros
FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
ON DELETE CASCADE;
