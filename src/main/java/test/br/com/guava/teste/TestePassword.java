package br.com.guava.teste;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestePassword {

	public static void main(String[] args) {
		System.out.println(passwordEncoder("admin"));
	}

	public static String passwordEncoder(String chave) {
		return new BCryptPasswordEncoder().encode(chave);
	}
}
