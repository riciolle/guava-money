package br.com.guava.api.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.guava.api.entity.Categoria;
import br.com.guava.api.repository.CategoriaRepository;
import br.com.guava.api.service.CategoriaService;

@Service
public class CategoriaServiceImpl implements CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Override
	public Categoria update(Long codigo ,Categoria categoria) {
		Categoria categoriaSaved = getByCodigo(codigo);
		BeanUtils.copyProperties(categoria, categoriaSaved, "codigo");
		return categoriaRepository.save(categoriaSaved);
	}
	
	private Categoria getByCodigo(Long codigo) {
		Categoria categoriaSaved = categoriaRepository.findOne(codigo);
		if (categoriaSaved == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return categoriaSaved;
	}

}
