package br.com.guava.api.guava.service.impl;

import org.springframework.beans.BeanUtils;
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
	
	@Override
	public Lancamento update(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoAux = buscarLancamentoExistente(codigo);
		if (!lancamento.getPessoa().equals(lancamentoAux.getPessoa())) {
			validarPessoa(lancamento);
		}
		
		BeanUtils.copyProperties(lancamento, lancamentoAux, "codigo");
		
		return lancamentoRepository.save(lancamentoAux);
	}

	private void validarPessoa(Lancamento lancamento) {
		Pessoa pessoa = null;
		if (lancamento.getPessoa().getCodigo() != null) {
			pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		}
		
		if (pessoa != null && pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
	}

	private Lancamento buscarLancamentoExistente(Long codigo) {
		Lancamento lancamento = lancamentoRepository.findOne(codigo);
		if (lancamento == null) {
			throw new IllegalArgumentException();
		}
		return lancamento;
	}
	
}
