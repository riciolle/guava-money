package br.com.guava.api.service;

import br.com.guava.api.entity.Pessoa;

public interface PessoaService {

	public Pessoa update(Long codigo, Pessoa pessoa);

	public void updateAtivo(Long codigo, Boolean ativo);

	Pessoa getByCodigo(Long codigo);

}
