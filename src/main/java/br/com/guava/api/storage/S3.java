package br.com.guava.api.storage;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.Tag;

import br.com.guava.api.config.property.GuavaApiProperty;

// @Component
public class S3 {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(S3.class); 
	
	// @Autowired
	private GuavaApiProperty property;

	// @Autowired
	private AmazonS3 amazonS3;
	
	public String salvarTemporariamente(MultipartFile file) {
		
		AccessControlList accessControlList = new AccessControlList();
		accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);
		
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(file.getContentType());
		objectMetadata.setContentLength(file.getSize());
		
		String nomeUnico = gerarNomeUnico(file.getOriginalFilename());
		
		try {
			PutObjectRequest putObjectRequest = new PutObjectRequest(
					property.getS3().getBucket(),
					nomeUnico,
					file.getInputStream(),
					objectMetadata).withAccessControlList(accessControlList);
			
			putObjectRequest.setTagging(new ObjectTagging(Arrays.asList(new Tag("expirar", "true"))));
			amazonS3.putObject(putObjectRequest);
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Arquivo enviado com sucesso para o S3 " , file.getOriginalFilename());
			}
			
			return nomeUnico;
			
		} catch (IOException ioException) {
			throw new RuntimeException("Problemas ao tentar enviar o arquivo para o S3. ", ioException);
		}
		
	}

	private String gerarNomeUnico(String originalFilename) {
		return UUID.randomUUID().toString() + "_" + originalFilename;
	}
}
