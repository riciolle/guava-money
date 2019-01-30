package br.com.guava.api.service.impl;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.guava.api.dto.LancamentoEstatisticaPessoaDTO;
import br.com.guava.api.entity.Lancamento;
import br.com.guava.api.entity.Pessoa;
import br.com.guava.api.entity.Usuario;
import br.com.guava.api.mail.Mailer;
import br.com.guava.api.repository.LancamentoRepository;
import br.com.guava.api.repository.PessoaRepository;
import br.com.guava.api.repository.UsuarioRepository;
import br.com.guava.api.service.LancamentoService;
import br.com.guava.api.service.exception.PessoaInexistenteOuInativaException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class LancamentoServiceImpl implements LancamentoService {
	
	private static final String DESTINATARIOS = "ROLE_PESQUISAR_LANCAMENTO";

	private static final Logger LOGGER = LoggerFactory.getLogger(LancamentoServiceImpl.class);
	
	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private Mailer mailer;
	
	@Scheduled(cron = "0 0 6 * * *")
	public void informarLancamentoVencido() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Preparando o envio de e-mails para aviso de lançamentos vencidos");
		}
		try {
			List<Lancamento> vencidos = lancamentoRepository.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());
			if (vencidos.isEmpty()) {
				LOGGER.info("Não existem Lançamentos vencidos para enviar e-mail");
				return;
			}
			LOGGER.info("Existem {} lançamentos vencidos.", vencidos.size());
			List<Usuario> destinatarios = usuarioRepository.findByPermissaosDescricao(DESTINATARIOS);
			if (destinatarios.isEmpty()) {
				LOGGER.info("Não existem destinatários para receber o e-mail de lançamentos vencidos");
				return;
			}
			mailer.warnAboutReleasesLosers(vencidos, destinatarios);
			LOGGER.info("Envio de e-amil de aviso concluído.");
		} catch (Exception exception) {
			
		}
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
