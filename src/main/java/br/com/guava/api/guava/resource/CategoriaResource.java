package br.com.guava.api.guava.resource;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.guava.api.guava.entity.Categoria;
import br.com.guava.api.guava.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@PostMapping
	public ResponseEntity<Categoria> save(@RequestBody Categoria categoria) {
		Categoria categoriaSaved = categoriaRepository.save(categoria);
		URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/codigo").buildAndExpand(categoriaSaved.getCodigo()).toUri();
//		response.setHeader("Location", uri.toASCIIString());
		
		return ResponseEntity.created(uri).body(categoriaSaved);
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Categoria> getByCodigo(@PathVariable Long codigo) {
		Categoria categoria = categoriaRepository.findOne(codigo);
		if (categoria != null) {
			return ResponseEntity.ok(categoria);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping
	public List<Categoria> findAll() {
		return categoriaRepository.findAll();
	}
}
