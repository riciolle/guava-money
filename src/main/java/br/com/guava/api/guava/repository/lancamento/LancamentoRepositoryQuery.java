package br.com.guava.api.guava.repository.lancamento;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.guava.api.guava.entity.Lancamento;
import br.com.guava.api.guava.repository.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {

	public Page<Lancamento> filter(LancamentoFilter lancamentoFilter,  Pageable pageable);
}
