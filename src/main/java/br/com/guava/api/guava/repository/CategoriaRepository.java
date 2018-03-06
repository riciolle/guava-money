package br.com.guava.api.guava.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.guava.api.guava.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
