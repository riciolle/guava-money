package br.com.guava.api.guava.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.guava.api.guava.entity.Pessoa;
import br.com.guava.api.guava.repository.PessoaRepository;
import br.com.guava.api.guava.service.PessoaService;

@Service
public class PessoaServiceImpl implements PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;

	@Override
	public Pessoa update(Long codigo ,Pessoa pessoa) {
		Pessoa pessoaSaved = getByCodigo(codigo);
		BeanUtils.copyProperties(pessoa, pessoaSaved, "codigo");
		return pessoaRepository.save(pessoaSaved);
	}
	
	@Override
	public void updateAtivo(Long codigo, Boolean ativo) {
		Pessoa pessoaSaved = getByCodigo(codigo);
		pessoaSaved.setAtivo(ativo);
		pessoaRepository.save(pessoaSaved);
		
	}
	
	private Pessoa getByCodigo(Long codigo) {
		Pessoa pessoaSaved = pessoaRepository.findOne(codigo);
		if (pessoaSaved == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return pessoaSaved;
	}
	
}
