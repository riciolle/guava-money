package br.com.guava.api.guava.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import br.com.guava.api.guava.config.property.GuavaApiProperty;

@Configuration
public class MailConfig {
	
	@Autowired
	private GuavaApiProperty guavaApiProperty;

	@Bean
	public JavaMailSender javaMailSender() {
		Properties properties = new Properties();

		properties.put("mail.transport.protocol", "smtp");
		properties.put("mail.smtp.auth", true);
		properties.put("mail.smtp.starttls.enable", true);
		properties.put("mail.smtp.connectiontimeout", 10000);
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setJavaMailProperties(properties);
		if (guavaApiProperty.getMail().getHost() != null &&
				guavaApiProperty.getMail().getPort() != null &&
				guavaApiProperty.getMail().getUserName() != null &&
				guavaApiProperty.getMail().getPassword() != null) {
			mailSender.setHost(guavaApiProperty.getMail().getHost());
			mailSender.setPort(guavaApiProperty.getMail().getPort());
			mailSender.setUsername(guavaApiProperty.getMail().getUserName());
			mailSender.setPassword(guavaApiProperty.getMail().getPassword());
			return mailSender;
		} else {
			throw new RuntimeException("Erro ao tentar acessar informações para envio de e-mail.");
		}
	}
}
