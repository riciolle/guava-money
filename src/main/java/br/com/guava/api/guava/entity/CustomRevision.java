package br.com.guava.api.guava.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Table;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import br.com.guava.api.guava.listener.CustomRevisionListener;

@Table(name = "CUSTOM_REVISION_ENTITY")
@RevisionEntity(CustomRevisionListener.class)

public class CustomRevision extends DefaultRevisionEntity implements Serializable{
    
    private static final long serialVersionUID = 3775550420286576001L;
    
    private Long userid;
    private String username;
    private String ipuser;
    private LocalDateTime dataAlteracao;

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIpuser() {
		return ipuser;
	}

	public void setIpuser(String ipuser) {
		this.ipuser = ipuser;
	}

	public LocalDateTime getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(LocalDateTime dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}
 
}
