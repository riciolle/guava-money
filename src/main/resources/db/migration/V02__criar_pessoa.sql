CREATE TABLE pessoa (
	codigo SERIAL,
	nome VARCHAR(64) NOT NULL,
	logradouro VARCHAR(64),
	numero VARCHAR(64),
	complemento VARCHAR(64),
	bairro VARCHAR(64),
	cep VARCHAR(64),
	cidade VARCHAR(64),
	estado VARCHAR(64),
	ativo BOOLEAN NOT NULL,
CONSTRAINT pk_codigo PRIMARY KEY (codigo)
);