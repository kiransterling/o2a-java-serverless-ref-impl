package o2a.java.serverless.ref.impl.config;

import java.util.Optional;

import javax.inject.Inject;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.env.Environment;

@Factory
public class Config {
	
	@Inject
    Environment environment;

	AmazonDynamoDB dynamoDbClient() {
		
		
		Optional<String> secretKey = environment.get("aws.secretkey", String.class);

		Optional<String> accessKey = environment.get("aws.accesskey", String.class);
		

		String amazonRegion = "ap-southeast-2";

		if (!secretKey.isPresent() || !accessKey.isPresent()) {

			throw new IllegalArgumentException("Aws credentials not provided");

		}

		BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey.get(), secretKey.get());

		return AmazonDynamoDBClientBuilder.standard().withRegion(amazonRegion)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

	}

	@Bean
	public DynamoDBMapper mapper() {

		return new DynamoDBMapper(dynamoDbClient());
	}

}
