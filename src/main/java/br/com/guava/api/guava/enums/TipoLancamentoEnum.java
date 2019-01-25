package br.com.guava.api.guava.enums;

import lombok.Getter;

@Getter
public enum TipoLancamentoEnum {

	RECEITA("Receita"), DESPESA("Despesa");

	private final String descricao;

	private TipoLancamentoEnum(String descricao) {
		this.descricao = descricao;
	}
}
