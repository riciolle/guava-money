package br.com.guava.api.guava.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.guava.api.guava.entity.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}
