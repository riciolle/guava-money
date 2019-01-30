package br.com.guava.api.security.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import br.com.guava.api.entity.Usuario;
import lombok.Getter;

public class UserSystem extends User {

	private static final long serialVersionUID = 1L;

	@Getter
	private Usuario usuario;
	
	public UserSystem(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
		super(usuario.getEmail(), usuario.getSenha(), authorities);
		this.usuario = usuario;
	}
}
