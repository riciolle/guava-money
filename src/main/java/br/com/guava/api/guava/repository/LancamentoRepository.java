package br.com.guava.api.guava.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.guava.api.guava.entity.Lancamento;
import br.com.guava.api.guava.repository.lancamento.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {

}
