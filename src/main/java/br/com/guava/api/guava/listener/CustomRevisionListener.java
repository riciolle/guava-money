package br.com.guava.api.guava.listener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.hibernate.envers.RevisionListener;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.guava.api.guava.entity.CustomRevision;

public class CustomRevisionListener implements RevisionListener {

	LocalDateTime date = LocalDateTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	String text = date.format(formatter);
	LocalDateTime parsedDate = LocalDateTime.parse(text, formatter);

	public void newRevision(Object revisionEntity) {
		CustomRevision revision = (CustomRevision) revisionEntity;

//		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		revision.setUsername(userDetails.getUsername());
//		revision.setIpuser(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr());

		revision.setDataAlteracao(parsedDate);
	}

}
