package br.com.guava.api.guava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import br.com.guava.api.guava.config.property.GuavaApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(GuavaApiProperty.class)
public class GuavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuavaApplication.class, args);
	}
}
