package br.com.guava.api.guava.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.guava.api.guava.entity.Categoria;
import br.com.guava.api.guava.event.ResourceCreatedEvent;
import br.com.guava.api.guava.repository.CategoriaRepository;
import br.com.guava.api.guava.service.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private CategoriaService categoriaService;
	
	// E um publicador de eventos da aplicação
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@PostMapping
	public ResponseEntity<Categoria> save(@Valid @RequestBody Categoria categoria, HttpServletResponse httpServletResponse) {
		Categoria categoriaSaved = categoriaRepository.save(categoria);
//		Source e quem gerou o evento e como esta com passando this significa que foi a classe CategoriaResource
		applicationEventPublisher.publishEvent(new ResourceCreatedEvent(this, httpServletResponse, categoria.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSaved);
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Categoria> getByCodigo(@PathVariable Long codigo) {
		Categoria categoria = categoriaRepository.findOne(codigo);
		return categoria != null ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
	}
	
	@GetMapping
	public List<Categoria> findAll() {
		return categoriaRepository.findAll();
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long codigo) {
		categoriaRepository.delete(codigo);
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<Categoria> update(@PathVariable Long codigo, @RequestBody Categoria categoria) {
		Categoria categoriSaved = categoriaService.update(codigo, categoria);
		return ResponseEntity.ok(categoriSaved);
	}
}
