package br.com.guava.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.guava.api.enums.TipoLancamentoEnum;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LancamentoEstatisticaDiaDTO {

	private TipoLancamentoEnum tipoLancamento;
	
	private LocalDate dia;
	
	private BigDecimal total;

	public LancamentoEstatisticaDiaDTO(TipoLancamentoEnum tipoLancamento, LocalDate dia, BigDecimal total) {
		this.tipoLancamento = tipoLancamento;
		this.dia = dia;
		this.total = total;
	}
	
	
}
