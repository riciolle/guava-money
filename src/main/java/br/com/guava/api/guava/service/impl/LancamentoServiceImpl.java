package br.com.guava.api.guava.service.impl;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.guava.api.guava.dto.LancamentoEstatisticaPessoaDTO;
import br.com.guava.api.guava.entity.Lancamento;
import br.com.guava.api.guava.entity.Pessoa;
import br.com.guava.api.guava.repository.LancamentoRepository;
import br.com.guava.api.guava.repository.PessoaRepository;
import br.com.guava.api.guava.service.LancamentoService;
import br.com.guava.api.guava.service.exception.PessoaInexistenteOuInativaException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Scheduled(cron = "0 0 6 * * *")
	public void informarLancamentoVencido() {
		
	}

	@Override
	public byte[] relatorioPorPessoa(LocalDate dtInicio, LocalDate dtFim) throws Exception {
		List<LancamentoEstatisticaPessoaDTO> dados = lancamentoRepository.porPessoa(dtInicio, dtFim);
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("DT_INICIO", Date.valueOf(dtInicio));
		parametros.put("DT_FIM", Date.valueOf(dtFim));
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
		InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/lancamentos-por-pessoa.jasper");
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,new JRBeanCollectionDataSource(dados));
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}

	@Override
	public Lancamento save(Lancamento lancamento) {
		Pessoa pessoaSaved = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		if (pessoaSaved == null || pessoaSaved.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		return lancamentoRepository.save(lancamento);
	}

	@Override
	public Lancamento update(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoAux = buscarLancamentoExistente(codigo);
		if (!lancamento.getPessoa().equals(lancamentoAux.getPessoa())) {
			validarPessoa(lancamento);
		}

		BeanUtils.copyProperties(lancamento, lancamentoAux, "codigo");

		return lancamentoRepository.save(lancamentoAux);
	}

	private void validarPessoa(Lancamento lancamento) {
		Pessoa pessoa = null;
		if (lancamento.getPessoa().getCodigo() != null) {
			pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		}

		if (pessoa != null && pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
	}

	private Lancamento buscarLancamentoExistente(Long codigo) {
		Lancamento lancamento = lancamentoRepository.findOne(codigo);
		if (lancamento == null) {
			throw new IllegalArgumentException();
		}
		return lancamento;
	}

}
