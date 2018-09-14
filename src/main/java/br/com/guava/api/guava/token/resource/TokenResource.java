package br.com.guava.api.guava.token.resource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.guava.api.guava.config.property.GuavaApiProperty;

@RestController
@RequestMapping("/token")
public class TokenResource {
	
	@Autowired
	private GuavaApiProperty guavaApiProperty;

	@DeleteMapping("/revoke")
	public void revoke(HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setHttpOnly(true);
		// CONFIGURAÇÃO FEITA A PARTIR DE PROPRIEDADES "application-prod.properties"
		cookie.setSecure(guavaApiProperty.getSecurity().isEnableHttps());
		cookie.setPath(request.getContextPath() + "/oauth/token");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		response.setStatus(HttpStatus.NO_CONTENT.value());
		
	}
}
