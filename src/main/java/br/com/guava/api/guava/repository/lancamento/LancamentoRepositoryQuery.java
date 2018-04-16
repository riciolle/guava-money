package br.com.guava.api.guava.repository.lancamento;

import java.util.List;

import br.com.guava.api.guava.entity.Lancamento;
import br.com.guava.api.guava.repository.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {

	public List<Lancamento> filter(LancamentoFilter lancamentoFilter);
}
