CREATE TABLE lancamento (
	codigo BIGINT PRIMARY KEY,
	descricao VARCHAR(100) NOT NULL,
	data_vencimento DATE NOT NULL,
	data_pagamento DATE,
	valor DECIMAL(10,2) NOT NULL,
	observacao VARCHAR(100),
	tipo VARCHAR(20) NOT NULL,
	codigo_categoria BIGINT NOT NULL,
	codigo_pessoa BIGINT NOT NULL,
	FOREIGN KEY (codigo_categoria) REFERENCES categoria(codigo),
	FOREIGN KEY (codigo_pessoa) REFERENCES pessoa(codigo)
);