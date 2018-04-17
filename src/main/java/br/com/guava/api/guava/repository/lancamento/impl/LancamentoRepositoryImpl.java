package br.com.guava.api.guava.repository.lancamento.impl;

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

import br.com.guava.api.guava.entity.Lancamento;
import br.com.guava.api.guava.repository.filter.LancamentoFilter;
import br.com.guava.api.guava.repository.lancamento.LancamentoRepositoryQuery;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Page<Lancamento> filter(LancamentoFilter lancamentoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		//CRIAR RESTRIÇÔES
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder , root);
		criteria.where(predicates);
		
		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		addRestrictionOfPageInQuery(query, pageable);
		
		
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}

	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder, Root<Lancamento> root) {
		
		List<Predicate> predicates = new ArrayList<>(0);
		
		if (!StringUtils.isEmpty(lancamentoFilter.getDescricao()) ) {
			predicates.add(builder.like(builder.lower(root.get("descricao")), "%" + lancamentoFilter.getDescricao() + "%"));
		}
		
		if (lancamentoFilter.getDataVencimentoDe() != null ) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("data_vencimento"),lancamentoFilter.getDataVencimentoDe()));
		}
		
		if (lancamentoFilter.getDataVencimentoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("data_vencimento"),lancamentoFilter.getDataVencimentoAte()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	private void addRestrictionOfPageInQuery(TypedQuery<Lancamento> query, Pageable pageable) {
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

}
