package br.com.guava.api.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "CUSTOM_REVISION_ENTITY")
@Getter @Setter
public class CustomRevision implements Serializable{
    
    private static final long serialVersionUID = 3775550420286576001L;
   
    private Long userid;
    private String username;
    private String ipuser;
    private LocalDateTime dataAlteracao;
 
    
}
