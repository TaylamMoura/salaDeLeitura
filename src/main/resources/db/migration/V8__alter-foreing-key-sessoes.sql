ALTER TABLE sessoes
DROP FOREIGN KEY sessoes_ibfk_2;

ALTER TABLE sessoes
ADD CONSTRAINT sessoes_ibfk_2
FOREIGN KEY (livro_id) REFERENCES livros(id)
ON DELETE CASCADE;
