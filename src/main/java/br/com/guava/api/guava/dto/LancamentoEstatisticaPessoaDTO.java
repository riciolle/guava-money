package br.com.guava.api.guava.dto;

import java.math.BigDecimal;

import br.com.guava.api.guava.entity.Pessoa;
import br.com.guava.api.guava.enums.TipoLancamentoEnum;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LancamentoEstatisticaPessoaDTO {

	private TipoLancamentoEnum tipo;
	
	private Pessoa pessoa;
		
	private BigDecimal total;
	
	public LancamentoEstatisticaPessoaDTO(TipoLancamentoEnum tipo, Pessoa pessoa, BigDecimal total) {	
		this.tipo = tipo;
		this.pessoa = pessoa;
		this.total = total;
	}
	
	
}
