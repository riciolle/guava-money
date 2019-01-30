package br.com.guava.api.repository.lancamento;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.guava.api.dto.LancamentoEstatisticaCategoriaDTO;
import br.com.guava.api.dto.LancamentoEstatisticaDiaDTO;
import br.com.guava.api.dto.LancamentoEstatisticaPessoaDTO;
import br.com.guava.api.entity.Lancamento;
import br.com.guava.api.repository.filter.LancamentoFilter;
import br.com.guava.api.repository.projection.ResumoLancamento;

public interface LancamentoRepositoryQuery {

	public List<LancamentoEstatisticaPessoaDTO> porPessoa(LocalDate dtInicio, LocalDate dtFim);
	public List<LancamentoEstatisticaCategoriaDTO> porCategoria(LocalDate mesReferencia);
	public List<LancamentoEstatisticaDiaDTO> porDia(LocalDate mesReferencia);
	
	public Page<Lancamento> filter(LancamentoFilter lancamentoFilter,  Pageable pageable);
	public Page<ResumoLancamento> resume(LancamentoFilter lancamentoFilter,  Pageable pageable);
}
