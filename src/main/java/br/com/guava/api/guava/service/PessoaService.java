package br.com.guava.api.guava.service;

import br.com.guava.api.guava.entity.Pessoa;

public interface PessoaService {

	public Pessoa update(Long codigo, Pessoa pessoa);

	public void updateAtivo(Long codigo, Boolean ativo);

	Pessoa getByCodigo(Long codigo);

}
