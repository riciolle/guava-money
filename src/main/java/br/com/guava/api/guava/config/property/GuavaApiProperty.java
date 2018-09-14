package br.com.guava.api.guava.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("guava")
public class GuavaApiProperty {
	
	@Getter @Setter
	private String originPermitida = "http://localhost:8080";

	private final Security security = new Security();

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

}
