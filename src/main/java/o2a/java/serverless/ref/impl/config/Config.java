package o2a.java.serverless.ref.impl.config;


import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;


@Factory
public class Config {

	@Bean
	AmazonDynamoDB amazonDynamoDB() {
		
		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();

		

		return AmazonDynamoDBClientBuilder.standard().withRegion("ap-southeast-2").withCredentials(credentialsProvider)
				.build();

	}

}
