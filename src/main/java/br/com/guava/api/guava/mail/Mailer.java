package br.com.guava.api.guava.mail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import br.com.guava.api.guava.repository.LancamentoRepository;

@Component
public class Mailer {

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private TemplateEngine thymeleaf;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@EventListener
	private void testeEmail(ApplicationReadyEvent applicationReadyEvent) {
		String template = "/mail/aviso-lancamento-vencidos.html";
		Map<String, Object> varivaveis = new HashMap<String, Object>();
		varivaveis.put("lancamentos", lancamentoRepository.findAll());	
		this.sendEmail("leoriciolle@gmail.com", Arrays.asList("leogeremias@hotmail.com"), "Testando", template, varivaveis);
		System.out.println("E-mail enviado");
	}
	
	public void sendEmail(String remetente, List<String> destinatario, String assunto, String template, Map<String, Object> varivaveis) {
		Context context = new Context(new Locale("pt", "BR"));
		varivaveis.entrySet().forEach(e -> context.setVariable(e.getKey(), e.getValue()));
		String mensagem = thymeleaf.process(template, context);
		this.sendEmail(remetente, destinatario, assunto, mensagem);
		
	}

	public void sendEmail(String remetente, List<String> destinatario, String assunto, String msg) {
		try {
		
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
			mimeMessageHelper.setFrom(remetente);
			mimeMessageHelper.setTo(destinatario.toArray(new String[destinatario.size()]));
			mimeMessageHelper.setSubject(assunto);
			mimeMessageHelper.setText(msg, true);
			
			javaMailSender.send(mimeMessage);

		} catch (MessagingException exception) {
			throw new RuntimeException("Problemas com envio de e-mail " + exception);
		}
	}
}
