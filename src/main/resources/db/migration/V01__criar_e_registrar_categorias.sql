CREATE TABLE categoria(
	codigo SERIAL,
	nome VARCHAR(64) NOT NULL,
 CONSTRAINT pk_codigo PRIMARY KEY (codigo)
);

INSERT INTO categoria (nome) values ('Vestido');