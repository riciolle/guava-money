package br.com.guava.api.guava.service;

import java.time.LocalDate;

import br.com.guava.api.guava.entity.Lancamento;

public interface LancamentoService {

	Lancamento save(Lancamento lancamento);

	Lancamento update(Long codigo, Lancamento lancamento);

	byte[] relatorioPorPessoa(LocalDate dtInicio, LocalDate dtFim) throws Exception;

}
