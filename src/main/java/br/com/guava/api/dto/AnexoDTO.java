package br.com.guava.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AnexoDTO {
	
	public AnexoDTO(String nome, String url) {
		this.nome = nome;
		this.url = url;
	}

	private String nome;
	
	private String url;
}
