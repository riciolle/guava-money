package br.com.guava.api.guava.exceptionhandle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// ControllerAdvice e um controlador que observa toda a aplicação
@ControllerAdvice
public class GuavaExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

		String mensagemUsuario = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
		List<Error> errors = Arrays.asList(new Error(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<Error> errors = createListError(ex.getBindingResult());
		return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
	}

	/**
	 * 
	 * Classe responsavel por tratar exceções especificas.
	 * @EmptyResultDataAccessException
	 * @author leonardo.aires
	 * @since 10-04-2018
	 */
	@ExceptionHandler({EmptyResultDataAccessException.class})
	public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest webRequest) {
		
		String mensagemUsuario = messageSource.getMessage("recurso.nao-encontrado", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
		List<Error> errors = Arrays.asList(new Error(mensagemUsuario, mensagemDesenvolvedor));
		
		return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.NOT_FOUND, webRequest);
	}
	
	/**
	 * 
	 * Classe responsavel por tratar exceções especificas.
	 * @DataIntegrityViolationException
	 * @author leonardo.aires
	 * @since 16-04-2018
	 */
	@ExceptionHandler({DataIntegrityViolationException.class})
	public ResponseEntity<Object> handleDataIntegrityViolationException(EmptyResultDataAccessException ex, WebRequest webRequest) {
		
		String mensagemUsuario = messageSource.getMessage("recurso.operacao-nao-permitida", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
		List<Error> errors = Arrays.asList(new Error(mensagemUsuario, mensagemDesenvolvedor));
		
		return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
	}
	
	private List<Error> createListError(BindingResult bindingResult) {
		List<Error> errors = new ArrayList<Error>(0);

		for(FieldError fieldError: bindingResult.getFieldErrors()) {
			String mensageUser = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
			String mensageDevelop = fieldError.toString();
			errors.add(new Error(mensageUser, mensageDevelop));
		}
		
		return errors;
	}

	public static class Error {

		private String mensageUser;
		private String mensageDevelop;

		public Error(String mensageUser, String mensageDevelop) {
			this.mensageUser = mensageUser;
			this.mensageDevelop = mensageDevelop;
		}

		public String getMensageUser() {
			return mensageUser;
		}

		public String getMensageDevelop() {
			return mensageDevelop;
		}

	}
}
