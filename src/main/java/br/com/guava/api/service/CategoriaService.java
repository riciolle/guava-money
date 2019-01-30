package br.com.guava.api.service;

import br.com.guava.api.entity.Categoria;

public interface CategoriaService {

	Categoria update(Long codigo, Categoria categoria);

}
