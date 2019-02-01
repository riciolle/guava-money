package br.com.guava.api.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.guava.api.entity.Pessoa;
import br.com.guava.api.repository.PessoaRepository;
import br.com.guava.api.service.PessoaService;

@Service
public class PessoaServiceImpl implements PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Override
	public Pessoa save(Pessoa pessoa) {
		try {
			pessoa.getContatos().forEach(c -> c.setPessoa(pessoa));
			return pessoaRepository.save(pessoa);
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	@Override
	public Pessoa update(Long codigo ,Pessoa pessoa) {
		Pessoa pessoaSaved = getByCodigo(codigo);
		
		pessoaSaved.getContatos().clear();
		pessoaSaved.getContatos().addAll(pessoa.getContatos());
		pessoaSaved.getContatos().forEach(c -> c.setPessoa(pessoaSaved));
		BeanUtils.copyProperties(pessoa, pessoaSaved, "codigo", "contatos");
		return pessoaRepository.save(pessoaSaved);
	}
	
	@Override
	public void updateAtivo(Long codigo, Boolean ativo) {
		Pessoa pessoaSaved = getByCodigo(codigo);
		pessoaSaved.setAtivo(ativo);
		pessoaRepository.save(pessoaSaved);
		
	}
	
	@Override
	public Pessoa getByCodigo(Long codigo) {
		Pessoa pessoaSaved = pessoaRepository.findOne(codigo);
		if (pessoaSaved == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return pessoaSaved;
	}
}
