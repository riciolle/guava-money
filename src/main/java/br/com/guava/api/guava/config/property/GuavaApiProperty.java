package br.com.guava.api.guava.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("guava")
public class GuavaApiProperty {

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
