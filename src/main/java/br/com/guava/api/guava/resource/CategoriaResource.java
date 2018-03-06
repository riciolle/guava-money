package br.com.guava.api.guava.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.guava.api.guava.entity.Categoria;
import br.com.guava.api.guava.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void save(@RequestBody Categoria categoria, HttpServletResponse response) {
		categoriaRepository.save(categoria);
		Serv
	}
	
	@GetMapping
	public List<Categoria> findAll() {
		return categoriaRepository.findAll();
	}
}
