INSERT INTO usuario (codigo, nome, email, senha) values (nextval('postgres_sequence'), 'Administrador', 'admin@algamoney.com', '$2a$10$X607ZPhQ4EgGNaYKt3n4SONjIv9zc.VMWdEuhCuba7oLAL5IvcL5.');
INSERT INTO usuario (codigo, nome, email, senha) values (nextval('postgres_sequence'), 'Maria Silva', 'maria@algamoney.com', '$2a$10$Zc3w6HyuPOPXamaMhh.PQOXvDnEsadztbfi6/RyZWJDzimE8WQjaq');

INSERT INTO permissao (codigo, descricao) values (nextval('postgres_sequence'), 'ROLE_CADASTRAR_CATEGORIA');
INSERT INTO permissao (codigo, descricao) values (nextval('postgres_sequence'), 'ROLE_PESQUISAR_CATEGORIA');

INSERT INTO permissao (codigo, descricao) values (nextval('postgres_sequence'), 'ROLE_CADASTRAR_PESSOA');
INSERT INTO permissao (codigo, descricao) values (nextval('postgres_sequence'), 'ROLE_REMOVER_PESSOA');
INSERT INTO permissao (codigo, descricao) values (nextval('postgres_sequence'), 'ROLE_PESQUISAR_PESSOA');

INSERT INTO permissao (codigo, descricao) values (nextval('postgres_sequence'), 'ROLE_CADASTRAR_LANCAMENTO');
INSERT INTO permissao (codigo, descricao) values (nextval('postgres_sequence'), 'ROLE_REMOVER_LANCAMENTO');
INSERT INTO permissao (codigo, descricao) values (nextval('postgres_sequence'), 'ROLE_PESQUISAR_LANCAMENTO');

-- admin
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values ((select codigo from usuario where nome = 'Administrador'), (select codigo from permissao where descricao = 'ROLE_CADASTRAR_CATEGORIA'));
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values ((select codigo from usuario where nome = 'Administrador'), (select codigo from permissao where descricao = 'ROLE_PESQUISAR_CATEGORIA'));
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values ((select codigo from usuario where nome = 'Administrador'), (select codigo from permissao where descricao = 'ROLE_CADASTRAR_PESSOA'));
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values ((select codigo from usuario where nome = 'Administrador'), (select codigo from permissao where descricao = 'ROLE_REMOVER_PESSOA'));
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values ((select codigo from usuario where nome = 'Administrador'), (select codigo from permissao where descricao = 'ROLE_PESQUISAR_PESSOA'));
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values ((select codigo from usuario where nome = 'Administrador'), (select codigo from permissao where descricao = 'ROLE_CADASTRAR_LANCAMENTO'));
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values ((select codigo from usuario where nome = 'Administrador'), (select codigo from permissao where descricao = 'ROLE_REMOVER_LANCAMENTO'));
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values ((select codigo from usuario where nome = 'Administrador'), (select codigo from permissao where descricao = 'ROLE_PESQUISAR_LANCAMENTO'));

-- maria
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values ((select codigo from usuario where nome = 'Maria Silva'), (select codigo from permissao where descricao = 'ROLE_PESQUISAR_CATEGORIA'));
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values ((select codigo from usuario where nome = 'Maria Silva'), (select codigo from permissao where descricao = 'ROLE_PESQUISAR_PESSOA'));
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values ((select codigo from usuario where nome = 'Maria Silva'), (select codigo from permissao where descricao = 'ROLE_PESQUISAR_LANCAMENTO'));


INSERT INTO pessoa (codigo, nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) values (nextval('postgres_sequence'),'João Silva', true, 'Rua do Abacaxi', '10', null, 'Brasil', '38.400-120', 'Uberlândia', 'MG');
INSERT INTO pessoa (codigo, nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) values (nextval('postgres_sequence'),'Maria Rita', true, 'Rua do Sabiá', '110', 'Apto 101', 'Colina', '11.400-120', 'Ribeirão Preto', 'SP');
INSERT INTO pessoa (codigo, nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) values (nextval('postgres_sequence'),'Pedro Santos', true, 'Rua da Bateria', '23', null, 'Morumbi', '54.212-120', 'Goiânia', 'GO');
INSERT INTO pessoa (codigo, nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) values (nextval('postgres_sequence'),'Ricardo Pereira', true, 'Rua do Motorista', '123', 'Apto 302', 'Aparecida', '38.400-120', 'Salvador', 'BA');
INSERT INTO pessoa (codigo, nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) values (nextval('postgres_sequence'),'Josué Mariano', true, 'Av Rio Branco', '321', null, 'Jardins', '56.400-120', 'Natal', 'RN');
INSERT INTO pessoa (codigo, nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) values (nextval('postgres_sequence'),'Pedro Barbosa', true, 'Av Brasil', '100', null, 'Tubalina', '77.400-120', 'Porto Alegre', 'RS');


INSERT INTO categoria (codigo,nome) values (nextval('postgres_sequence'), 'Lazer');
INSERT INTO categoria (codigo,nome) values (nextval('postgres_sequence'), 'Alimentação');
INSERT INTO categoria (codigo,nome) values (nextval('postgres_sequence'), 'Supermercado');
INSERT INTO categoria (codigo,nome) values (nextval('postgres_sequence'), 'Farmácia');
INSERT INTO categoria (codigo,nome) values (nextval('postgres_sequence'), 'Transporte');
INSERT INTO categoria (codigo,nome) values (nextval('postgres_sequence'), 'Moradia');
INSERT INTO categoria (codigo,nome) values (nextval('postgres_sequence'), 'Automóvel');
INSERT INTO categoria (codigo,nome) values (nextval('postgres_sequence'), 'Vestuário');


INSERT INTO lancamento (codigo, descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa)
	values (nextval('postgres_sequence'), 'Salário mensal', '2017-06-10', null, 6500.00, 'Distribuição de lucros', 'RECEITA', (select codigo from categoria where nome = 'Lazer'), (select codigo from pessoa where nome = 'João Silva'));
INSERT INTO lancamento (codigo, descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa)
	values (nextval('postgres_sequence'), 'Bahamas', '2017-02-10', '2017-02-10', 100.32, null, 'DESPESA', (select codigo from categoria where nome = 'Alimentação'), (select codigo from pessoa where nome = 'Maria Rita'));
INSERT INTO lancamento (codigo, descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa)
	values (nextval('postgres_sequence'), 'Top Club', '2017-06-10', null, 120, null, 'RECEITA', (select codigo from categoria where nome = 'Supermercado'), (select codigo from pessoa where nome = 'Maria Rita'));
INSERT INTO lancamento (codigo, descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa)
	values (nextval('postgres_sequence'), 'CEMIG', '2017-02-10', '2017-02-10', 110.44, 'Geração', 'RECEITA', (select codigo from categoria where nome = 'Transporte'), (select codigo from pessoa where nome = 'Pedro Barbosa'));

