package br.com.guava.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.guava.api.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
