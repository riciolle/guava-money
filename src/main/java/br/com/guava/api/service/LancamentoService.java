package br.com.guava.api.service;

import java.time.LocalDate;

import br.com.guava.api.entity.Lancamento;

public interface LancamentoService {

	Lancamento save(Lancamento lancamento);

	Lancamento update(Long codigo, Lancamento lancamento);

	byte[] relatorioPorPessoa(LocalDate dtInicio, LocalDate dtFim) throws Exception;

}
