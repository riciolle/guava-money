package br.com.guava.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("guava")
public class GuavaApiProperty {
	
	@Getter @Setter
	private String originPermitida = "http://localhost:4200";

	private final Security security = new Security();
	
	private final Mail mail = new Mail();
	
	private final S3 s3 = new S3();
	
	public Mail getMail() {

		return mail;
	}

	public Security getSecurity() {

		return security;
	}
	
	public S3 getS3() {
		return s3;
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

	@Getter @Setter
	public static class S3 {
		
		private String accessKeyId;
		
		private String secretAccessKey;
		
		private String bucket = "gm-guavamoney-file";
	}
}
