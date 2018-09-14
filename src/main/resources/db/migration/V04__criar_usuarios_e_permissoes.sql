CREATE TABLE usuario (
	codigo SERIAL NOT NULL,
	nome VARCHAR(64) NOT NULL,
	email VARCHAR(64) NOT NULL,
	senha VARCHAR(192) NOT NULL,
CONSTRAINT pk_codigo_usuario PRIMARY KEY (codigo)	
);

CREATE TABLE permissao (
	codigo BIGINT,
	descricao VARCHAR(64) NOT NULL,
CONSTRAINT pk_codigo_permissao PRIMARY KEY (codigo)
);

CREATE TABLE usuario_permissao (
	codigo_usuario BIGINT NOT NULL,
	codigo_permissao BIGINT NOT NULL,
PRIMARY KEY (codigo_usuario, codigo_permissao),
FOREIGN KEY (codigo_usuario) REFERENCES usuario(codigo),
FOREIGN KEY (codigo_permissao) REFERENCES permissao(codigo)
);