
ALTER TABLE sessoes
DROP COLUMN url_capa;

ALTER TABLE sessoes
MODIFY COLUMN pagina_inicial INT NULL;
