package br.com.guava.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.guava.api.enums.TipoLancamentoEnum;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LancamentoEstatisticaDiaDTO {

	private TipoLancamentoEnum tipo;
	
	private LocalDate dia;
	
	private BigDecimal total;
	
	public LancamentoEstatisticaDiaDTO(TipoLancamentoEnum tipo, LocalDate dia, BigDecimal total) {
		this.tipo = tipo;
		this.dia = dia;
		this.total = total;
	}
	
	
}
