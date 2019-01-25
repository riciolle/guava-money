package br.com.guava.api.guava.repository.lancamento.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import br.com.guava.api.guava.dto.LancamentoEstatisticaCategoriaDTO;
import br.com.guava.api.guava.dto.LancamentoEstatisticaDiaDTO;
import br.com.guava.api.guava.dto.LancamentoEstatisticaPessoaDTO;
import br.com.guava.api.guava.entity.Lancamento;
import br.com.guava.api.guava.repository.filter.LancamentoFilter;
import br.com.guava.api.guava.repository.lancamento.LancamentoRepositoryQuery;
import br.com.guava.api.guava.repository.projection.ResumoLancamento;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public Page<Lancamento> filter(LancamentoFilter lancamentoFilter, Pageable pageable) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);

		// CRIAR RESTRIÇÔES
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);

		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		addRestrictionOfPageInQuery(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}

	@Override
	public Page<ResumoLancamento> resume(LancamentoFilter lancamentoFilter, Pageable pageable) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);

		Root<Lancamento> root = criteria.from(Lancamento.class);

		criteria.select(builder.construct(ResumoLancamento.class, 
				root.get("codigo"), 
				root.get("descricao"), 
				root.get("dataVencimento"), 
				root.get("dataPagamento"), 
				root.get("valor"),
				root.get("tipo"), 
				root.get("categoria").get("nome"), 
				root.get("pessoa").get("nome")));

		// CRIAR RESTRIÇÔES
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);

		TypedQuery<ResumoLancamento> query = manager.createQuery(criteria);
		addRestrictionOfPageInQuery(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}

	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder, Root<Lancamento> root) {

		List<Predicate> predicates = new ArrayList<>(0);

		if (!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
			predicates.add(builder.like(builder.lower(root.get("descricao")), "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
		}

		if (lancamentoFilter.getDataVencimentoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("dataVencimento"), lancamentoFilter.getDataVencimentoDe()));
		}

		if (lancamentoFilter.getDataVencimentoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("dataVencimento"), lancamentoFilter.getDataVencimentoAte()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private void addRestrictionOfPageInQuery(TypedQuery<?> query, Pageable pageable) {

		Integer pageActual = pageable.getPageNumber();
		Integer totalRecordPerPage = pageable.getPageSize();
		Integer firstRecordFromPage = pageActual * totalRecordPerPage;

		query.setFirstResult(firstRecordFromPage);
		query.setMaxResults(totalRecordPerPage);
	}

	private Long total(LancamentoFilter lancamentoFilter) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);

		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		criteria.select(builder.count(root));

		return manager.createQuery(criteria).getSingleResult();
	}

	@Override
	public List<LancamentoEstatisticaCategoriaDTO> porCategoria(LocalDate mesReferencia) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery<LancamentoEstatisticaCategoriaDTO> criteriaQuery = criteriaBuilder
				.createQuery(LancamentoEstatisticaCategoriaDTO.class);
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaCategoriaDTO.class,
				root.get("categoria"), criteriaBuilder.sum(root.get("valor"))));

		LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
		LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());

		criteriaQuery.where(criteriaBuilder.greaterThanOrEqualTo(root.get("dataVencimento"), primeiroDia),
							criteriaBuilder.lessThanOrEqualTo(root.get("dataVencimento"), ultimoDia));

		criteriaQuery.groupBy(root.get("categoria"));
		TypedQuery<LancamentoEstatisticaCategoriaDTO> typedQuery = manager.createQuery(criteriaQuery);

		return typedQuery.getResultList();
	}
	
	@Override
	public List<LancamentoEstatisticaDiaDTO> porDia(LocalDate mesReferencia) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery<LancamentoEstatisticaDiaDTO> criteriaQuery = criteriaBuilder.createQuery(LancamentoEstatisticaDiaDTO.class);
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaDiaDTO.class,
													   root.get("tipo"), 
													   criteriaBuilder.sum(root.get("dataVencimento"))));

		LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
		LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());

		criteriaQuery.where(criteriaBuilder.greaterThanOrEqualTo(root.get("dataVencimento"), primeiroDia),
							criteriaBuilder.lessThanOrEqualTo(root.get("dataVencimento"), ultimoDia));

		criteriaQuery.groupBy(root.get("tipo"),root.get("dataVencimento"));
		TypedQuery<LancamentoEstatisticaDiaDTO> typedQuery = manager.createQuery(criteriaQuery);

		return typedQuery.getResultList();
	}

	@Override
	public List<LancamentoEstatisticaPessoaDTO> porPessoa(LocalDate dtInicio, LocalDate dtFim) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery<LancamentoEstatisticaPessoaDTO> criteriaQuery = criteriaBuilder.createQuery(LancamentoEstatisticaPessoaDTO.class);
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaPessoaDTO.class,
													   root.get("tipo"),
													   root.get("pessoa"),
													   criteriaBuilder.sum(root.get("valor"))));

		criteriaQuery.where(criteriaBuilder.greaterThanOrEqualTo(root.get("dataVencimento"), dtInicio),
							criteriaBuilder.lessThanOrEqualTo(root.get("dataVencimento"), dtFim));

		criteriaQuery.groupBy(root.get("tipo"),root.get("pessoa"));
		TypedQuery<LancamentoEstatisticaPessoaDTO> typedQuery = manager.createQuery(criteriaQuery);

		return typedQuery.getResultList();
	}

}
