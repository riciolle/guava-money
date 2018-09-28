CREATE TABLE pessoa (
	codigo BIGINT PRIMARY KEY,
	nome VARCHAR(100) NOT NULL,
	ativo BOOLEAN NOT NULL,
	logradouro VARCHAR(100),
	numero VARCHAR(10),
	complemento VARCHAR(50),
	bairro VARCHAR(50),
	cep VARCHAR(20),
	cidade VARCHAR(50),
	estado VARCHAR(30)
);