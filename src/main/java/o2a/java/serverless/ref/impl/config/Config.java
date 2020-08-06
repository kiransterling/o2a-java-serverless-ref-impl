package o2a.java.serverless.ref.impl.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Value;

@Factory
public class Config {

	@Value("${amazon.region}")
	private String amazonRegion;

	public AWSCredentialsProvider amazonAWSCredentials() {
		return new ProfileCredentialsProvider();
	}

	@Bean
	public DynamoDBMapperConfig dynamoDBMapperConfig() {
		return DynamoDBMapperConfig.DEFAULT;
	}

	@Primary
	@Bean
	public IDynamoDBMapper mapper(AmazonDynamoDB amazonDynamoDB, DynamoDBMapperConfig config) {
		return new DynamoDBMapper(amazonDynamoDB, config);
	}

	@Primary
	@Bean
	public AmazonDynamoDB client() {
		return AmazonDynamoDBClientBuilder.standard().withRegion(amazonRegion).withCredentials(amazonAWSCredentials())
				.build();
	}

}
