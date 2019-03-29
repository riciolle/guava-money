package br.com.guava.api.resource;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.guava.api.dto.AnexoDTO;
import br.com.guava.api.dto.LancamentoEstatisticaCategoriaDTO;
import br.com.guava.api.dto.LancamentoEstatisticaDiaDTO;
import br.com.guava.api.entity.Lancamento;
import br.com.guava.api.event.ResourceCreatedEvent;
import br.com.guava.api.exceptionhandle.GuavaExceptionHandler.Error;
import br.com.guava.api.repository.LancamentoRepository;
import br.com.guava.api.repository.filter.LancamentoFilter;
import br.com.guava.api.repository.projection.ResumoLancamento;
import br.com.guava.api.service.LancamentoService;
import br.com.guava.api.service.exception.PessoaInexistenteOuInativaException;
import br.com.guava.api.storage.S3;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	// E UM PUBLICADOR DE EVENTOS DA APLICAÇÃO
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
		
	@Autowired
	private S3 s3;
	
	@Autowired
	private MessageSource messageSource;

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Lancamento> save(@Valid @RequestBody Lancamento lancamento, HttpServletResponse httpServletResponse) {
		Lancamento categoriaSaved = lancamentoService.save(lancamento);
		// SOURCE E QUEM GEROU O EVENTO E COMO ESTA COM PASSANDO THIS SIGNIFICA QUE FOI A CLASSE LANCAMENTORESOURCE
		applicationEventPublisher.publishEvent(new ResourceCreatedEvent(this, httpServletResponse, lancamento.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSaved);
	}

	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<Lancamento> getByCodigo(@PathVariable Long codigo) {
		Lancamento categoria = lancamentoRepository.findOne(codigo);
		return categoria != null ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public List<Lancamento> findAll() {
		return lancamentoRepository.findAll();
	}
	
	@GetMapping("consultar")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public Page<Lancamento> find(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.filter(lancamentoFilter, pageable);
	}
	
	@GetMapping(params="resume")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public Page<ResumoLancamento> resume(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.resume(lancamentoFilter, pageable);
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('delete')")
	public void delete(@PathVariable Long codigo) {
		lancamentoRepository.delete(codigo);
	}
	
	@ExceptionHandler({ PessoaInexistenteOuInativaException.class })
	public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {
		String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Error> erros = Arrays.asList(new Error(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}
	
	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Lancamento> update(@PathVariable Long codigo, @Valid @RequestBody Lancamento lancamento) {
		try {
			Lancamento lancamentoAux = lancamentoService.update(codigo, lancamento);
			return ResponseEntity.ok(lancamentoAux);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/estatisticas/por-categoria")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public List<LancamentoEstatisticaCategoriaDTO> porCategoria() {
		return this.lancamentoRepository.porCategoria(LocalDate.now());
	}
	
	@GetMapping("/estatisticas/por-dia")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public List<LancamentoEstatisticaDiaDTO> porDia() {
		return this.lancamentoRepository.porDia(LocalDate.now());
	}
	
	@GetMapping("/relatorio/por-pessoa")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<byte[]> relatorioPorPessoa(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dtInicio, 
													 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dtFim) {
		byte[] relatorio = null;
		try {
			relatorio = lancamentoService.relatorioPorPessoa(dtInicio, dtFim);
		} catch (Exception erros) {
			erros.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE).body(relatorio);		
	}
	
	@PostMapping("/anexo")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public AnexoDTO uploadAnexo(@RequestParam MultipartFile anexo) throws IOException {
		String nome = s3.salvarTemporariamente(anexo);
		return new AnexoDTO(nome, s3.configurarUrl(nome));
	}

}
