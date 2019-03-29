package br.com.guava.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.Tag;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;
import com.amazonaws.services.s3.model.lifecycle.LifecycleTagPredicate;

import br.com.guava.api.config.property.GuavaApiProperty;

@Configuration
public class AmazonS3Config {

	@Autowired
	private GuavaApiProperty property;

	@Bean
	public AmazonS3 amazonS3() {
		try {

			AWSCredentials awsCredentials = new BasicAWSCredentials(property.getS3().getAccessKeyId(),
					property.getS3().getSecretAccessKey());
			AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
					.withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).withRegion(Regions.US_EAST_1)
					.build();

			if (!amazonS3.doesBucketExistV2(property.getS3().getBucket())) {
				amazonS3.createBucket(new CreateBucketRequest(property.getS3().getBucket()));

				BucketLifecycleConfiguration.Rule regraExpiracao = new BucketLifecycleConfiguration.Rule()
						.withId("Regra de expiração de arquivos temporários")
						.withFilter(new LifecycleFilter(new LifecycleTagPredicate(new Tag("expirar", "true"))))
						.withExpirationInDays(1).withStatus(BucketLifecycleConfiguration.ENABLED);
				BucketLifecycleConfiguration configuration = new BucketLifecycleConfiguration()
						.withRules(regraExpiracao);
				amazonS3.setBucketLifecycleConfiguration(property.getS3().getBucket(), configuration);
			}

			return amazonS3;
		} catch (Exception exception) {
			throw new RuntimeException("Erro ao acessar AWS da Amazon " + exception);
		}
	}

}
