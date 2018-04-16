package br.com.guava.api.guava.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.guava.api.guava.entity.Lancamento;
import br.com.guava.api.guava.entity.Pessoa;
import br.com.guava.api.guava.repository.LancamentoRepository;
import br.com.guava.api.guava.repository.PessoaRepository;
import br.com.guava.api.guava.service.LancamentoService;
import br.com.guava.api.guava.service.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoServiceImpl implements LancamentoService {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;

	@Override
	public Lancamento save(Lancamento lancamento ) {
		Pessoa pessoaSaved = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		if (pessoaSaved == null || pessoaSaved.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		return lancamentoRepository.save(lancamento);
	}
	
}
