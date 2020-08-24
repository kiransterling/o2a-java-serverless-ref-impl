package o2a.java.serverless.ref.impl.controller;

import javax.inject.Inject;

import org.junit.After;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.agorapulse.micronaut.aws.dynamodb.DynamoDBService;
import com.agorapulse.micronaut.aws.dynamodb.DynamoDBServiceProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import o2a.java.serverless.ref.impl.model.Student;
import o2a.java.serverless.ref.impl.service.IDynamoDBService;

@MicronautTest
@Testcontainers
public class SimpleControllerTestWithLocalStack {

	public ApplicationContext ctx;

	@Inject
	private IDynamoDBService dynamoDBService;

	@Inject
	EmbeddedServer server;

	// private AmazonDynamoDB amazonDynamoDB;

	@Inject
	@Client("/student")
	RxHttpClient client;

	@Container
	public LocalStackContainer localstack = new LocalStackContainer()
			.withServices(LocalStackContainer.Service.DYNAMODB);

	@BeforeEach
	public void setup() {
		AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClient.builder()
				.withEndpointConfiguration(localstack.getEndpointConfiguration(LocalStackContainer.Service.DYNAMODB))
				.withCredentials(localstack.getDefaultCredentialsProvider()).build();

		IDynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);

		ctx = ApplicationContext.build().build();
		ctx.registerSingleton(AmazonDynamoDB.class, amazonDynamoDB);
		ctx.registerSingleton(IDynamoDBMapper.class, mapper);
		ctx.start();
	}

	@After
	public void cleanup() {
		if (ctx != null) {
			ctx.close();
		}
	}

	@Test
	public void testSomething() {
		DynamoDBServiceProvider provider = ctx.getBean(DynamoDBServiceProvider.class);
		DynamoDBService<Student> student = provider.findOrCreate(Student.class);
		IDynamoDBService dbService = ctx.getBean(IDynamoDBService.class);

		Student studentObj = new Student();
		studentObj.setStudentId("1");
		studentObj.setFirstName("Sam");
		studentObj.setLastName("Smith");
		studentObj.setAge(35);

		studentObj = dbService.save(studentObj);

		System.out.println("studentObj = " + studentObj);

		Student st = dbService.get("1", "Smith");

		Assertions.assertEquals("Sam", st.getFirstName());

	}
}
