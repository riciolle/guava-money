package br.com.guava.api.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import br.com.guava.api.config.property.GuavaApiProperty;

@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken> {
	
	@Autowired
	private GuavaApiProperty guavaApiProperty;

	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter arg1, MediaType arg2, Class<? extends HttpMessageConverter<?>> arg3, ServerHttpRequest request,
			ServerHttpResponse response) {

		String refreshToken = body.getRefreshToken().getValue();
		HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
		HttpServletResponse res = ((ServletServerHttpResponse) response).getServletResponse();
		
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body;
		// Cria o Cookie com Refresh Token
		addRefreshTokenCookie(refreshToken, req, res);
		// Remove o Refresh Token do retorno original  
		removeRefreshTokenToBody(token);
		return body;
	}

	private void removeRefreshTokenToBody(DefaultOAuth2AccessToken token) {
		token.setRefreshToken(null);
	}

	private void addRefreshTokenCookie(String refreshToken, HttpServletRequest req, HttpServletResponse res) {
		
		Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
		// E acessivel apenas em HTTP
		refreshTokenCookie.setHttpOnly(true);
		// Cookie deve apenas funcionar em HTTPS (SEMPRE SEGURO) 
		// CONFIGURAÇÃO FEITA A PARTIR DE PROPRIEDADES "application-prod.properties"
		refreshTokenCookie.setSecure(guavaApiProperty.getSecurity().isEnableHttps());
		// Caminho que deve ser enviado quando for feita a requisção
		refreshTokenCookie.setPath(req.getContextPath() + "/oauth/token");
		// Em quantos dias esse cookie ira espeirar 
		refreshTokenCookie.setMaxAge(30);
		// Adiciona o Cookie na resposta
		res.addCookie(refreshTokenCookie);

	}

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> arg1) {
		return returnType.getMethod().getName().equals("postAccessToken");
	}

}
