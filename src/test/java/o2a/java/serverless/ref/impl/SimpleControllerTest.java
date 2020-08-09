package o2a.java.serverless.ref.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.localstack.LocalStackContainer;

import com.agorapulse.micronaut.aws.dynamodb.DynamoDBService;
import com.agorapulse.micronaut.aws.dynamodb.DynamoDBServiceProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import cloud.localstack.LocalstackTestRunner;
import cloud.localstack.TestUtils;
import cloud.localstack.docker.annotation.LocalstackDockerProperties;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import o2a.java.serverless.ref.impl.model.Student;
import o2a.java.serverless.ref.impl.shared.Utils;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.junit.runner.RunWith;
import cloud.localstack.docker.LocalstackDockerExtension;

@MicronautTest(application = Application.class)
@ExtendWith(LocalstackDockerExtension.class)
@LocalstackDockerProperties(services = { "dynamodb" })
public class SimpleControllerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleControllerTest.class);

	public static ApplicationContext ctx;

	/*
	 * @Rule public static LocalStackContainer localstack = new
	 * LocalStackContainer("latest")
	 * .withServices(LocalStackContainer.Service.DYNAMODB);
	 */
	
	@Inject
	EmbeddedServer server;

	@Inject
	@Client("/api")
	RxHttpClient client;

	@BeforeAll
	public static void setup() throws InterruptedException {

		/*
		 * AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClient.builder()
		 * .withEndpointConfiguration(localstack.getEndpointConfiguration(
		 * LocalStackContainer.Service.DYNAMODB))
		 * .withCredentials(localstack.getDefaultCredentialsProvider()).build();
		 */
		
		System.out.println("k");
		AmazonDynamoDB amazonDynamoDB = TestUtils.getClientDynamoDB();
				
		System.out.println("k1");
		

		// Creating Student table
		Utils.createTable(amazonDynamoDB, "Student");

		ctx = ApplicationContext.build().build();
		ctx.registerSingleton(AmazonDynamoDB.class, amazonDynamoDB);

		ctx.start();
	}

	@AfterAll
	public static void cleanup() {
		if (ctx != null) {
			ctx.close();
		}
	}

	@Test
	public void StudentTest() {

		DynamoDBServiceProvider provider = ctx.getBean(DynamoDBServiceProvider.class);
		DynamoDBService<Student> dynamoDBService = provider.findOrCreate(Student.class);

		Student student = new Student();
		student.setStudentId("1");
		student.setFirstName("Sam");
		student.setLastName("Smith");
		student.setAge(35);

		String st = client.toBlocking().retrieve(HttpRequest.GET("/hi"), String.class);
		System.out.println(st);

		dynamoDBService.save(student);

		dynamoDBService.get("1", "Smith");

		dynamoDBService.delete(student);

	}

}
