package o2a.java.serverless.ref.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agorapulse.micronaut.aws.dynamodb.DynamoDBService;
import com.agorapulse.micronaut.aws.dynamodb.DynamoDBServiceProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import cloud.localstack.docker.LocalstackDockerExtension;

@MicronautTest(application = Application.class)
@ExtendWith(LocalstackDockerExtension.class)
@LocalstackDockerProperties(services = { "dynamodb" })
public class SimpleControllerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleControllerTest.class);

	public static ApplicationContext ctx;

	@Inject
	EmbeddedServer server;

	@Inject
	@Client("/api")
	RxHttpClient client;

	static DynamoDBServiceProvider provider;

	static DynamoDBService<Student> dynamoDBService;

	@BeforeAll
	public static void setup() throws InterruptedException {

		AmazonDynamoDB amazonDynamoDB = TestUtils.getClientDynamoDB();

		// Creating Student table
		Utils.createTable(amazonDynamoDB, "Student");

		ctx = ApplicationContext.build().build();

		ctx.registerSingleton(AmazonDynamoDB.class, amazonDynamoDB);

		ctx.start();

		// Obtain the provider bean
		provider = ctx.getBean(DynamoDBServiceProvider.class);

		// Obtain DynamoDBService for particular DynamoDB entity
	    dynamoDBService = provider.findOrCreate(Student.class);

	}

	@AfterAll
	public static void cleanup() {

		// Clean up Application context after the test

		if (ctx != null) {
			ctx.close();
		}

	}

	@Test
	public void StudentTest() {

		Student student = new Student();
		student.setStudentId("1");
		student.setFirstName("Sam");
		student.setLastName("Smith");
		student.setAge(35);

		System.out.println(dynamoDBService.save(student).toString());
		System.out.println(dynamoDBService.get(student.getStudentId(), student.getLastName()).toString());
		dynamoDBService.delete(student);

		//Student st = client.toBlocking().retrieve(HttpRequest.POST("/createStudent", student), Student.class);

		// System.out.println(st.toString());

		//st = client.toBlocking().retrieve(HttpRequest.GET("/getOneStudentDetails/1/Smith"), Student.class);

		//System.out.println(st.toString());

		//String st1 = client.toBlocking().retrieve(HttpRequest.DELETE("/deleteStudent/1/Smith"), String.class);

		//System.out.println(st1);

	}

}
