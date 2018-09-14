package br.com.guava.api.guava.repository.lancamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.guava.api.guava.entity.Lancamento;
import br.com.guava.api.guava.repository.filter.LancamentoFilter;
import br.com.guava.api.guava.repository.projection.ResumoLancamento;

public interface LancamentoRepositoryQuery {

	public Page<Lancamento> filter(LancamentoFilter lancamentoFilter,  Pageable pageable);
	public Page<ResumoLancamento> resume(LancamentoFilter lancamentoFilter,  Pageable pageable);
}
