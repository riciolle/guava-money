package br.com.guava.api.guava.config.token;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import br.com.guava.api.guava.security.entity.UserSystem;

public class CustomTokenEnchancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		UserSystem userSytem = (UserSystem) authentication.getPrincipal();
		
		// ADICIONAR O NOME DE USUARIO LOGADO NO SISTEMA
		Map<String, Object> addInfo = new HashMap<>();
		addInfo.put("nome", userSytem.getUsuario().getNome());
		// ULTIMA DATA DE LOGIN 
		addInfo.put("ultimo_login", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
		
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(addInfo);
		
		return accessToken;
		
	}

}
