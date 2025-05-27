-- Alteração do tempo_leitura para INT (segundos)
ALTER TABLE sessoes
MODIFY COLUMN tempo_leitura INT NOT NULL;

-- Adicionando coluna para marcar se um livro foi concluído
ALTER TABLE livros
ADD COLUMN livro_finalizado BOOLEAN NOT NULL DEFAULT FALSE;
