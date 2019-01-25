package br.com.guava.api.guava.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("guava")
public class GuavaApiProperty {
	
	@Getter @Setter
	private String originPermitida = "http://localhost:4200";

	private final Security security = new Security();
	
	private final Mail mail = new Mail();
	
	public Mail getMail() {

		return mail;
	}

	public Security getSecurity() {

		return security;
	}

	public static class Security {

		private boolean enableHttps;

		public boolean isEnableHttps() {

			return enableHttps;
		}

		public void setEnableHttps(boolean enableHttps) {

			this.enableHttps = enableHttps;
		}
	}
	
	@Getter @Setter
	public static class Mail {
		
		private String host;
		
		private Integer port;
		
		private String userName;
		
		private String password;
	}

}
