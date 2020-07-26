package o2a.java.serverless.ref.impl.config;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;


@Factory
public class Config {

	@Bean
	DynamoDbClient dynamoDbClient() {

		Region region = Region.AP_NORTHEAST_2;

		return DynamoDbClient.builder().region(region)
				.credentialsProvider(EnvironmentVariableCredentialsProvider.create()).build();

	}

}
