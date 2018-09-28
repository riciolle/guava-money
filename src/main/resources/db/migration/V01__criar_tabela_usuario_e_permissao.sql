CREATE SEQUENCE postgres_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE usuario (
	codigo BIGINT PRIMARY KEY,
	nome VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	senha VARCHAR(150) NOT NULL
);

CREATE TABLE permissao (
	codigo BIGINT PRIMARY KEY,
	descricao VARCHAR(50) NOT NULL
);

CREATE TABLE usuario_permissao(
	codigo_usuario BIGINT NOT NULL,
	codigo_permissao BIGINT NOT NULL,
	PRIMARY KEY (codigo_usuario, codigo_permissao),
	FOREIGN KEY (codigo_usuario) REFERENCES usuario(codigo),
	FOREIGN KEY (codigo_permissao) REFERENCES permissao(codigo)
);