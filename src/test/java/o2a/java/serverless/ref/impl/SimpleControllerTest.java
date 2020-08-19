package o2a.java.serverless.ref.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agorapulse.micronaut.aws.dynamodb.DynamoDBServiceProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

import cloud.localstack.docker.LocalstackDockerExtension;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import o2a.java.serverless.ref.impl.model.Student;
import o2a.java.serverless.ref.impl.service.IDynamoDBService;

@MicronautTest(application = Application.class)
@ExtendWith(LocalstackDockerExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class SimpleControllerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleControllerTest.class);

	@Inject
	private ApplicationContext ctx;

	Student student;

	private IDynamoDBService dynamoDBService;

	@Inject
	EmbeddedServer server;

	private AmazonDynamoDB amazonDynamoDB;

	@Inject
	@Client("/api")
	RxHttpClient client;

	@Inject
	private DynamoDBServiceProvider provider;

//	DynamoDBService<Student> dynamoDBService;

	@MockBean(IDynamoDBService.class)
	IDynamoDBService dynamoDBService() {
		return mock(IDynamoDBService.class);
	}

	@BeforeAll
	public void setup() throws InterruptedException {

		// dynamoDBService = ctx.getBean(IDynamoDBService.class);
		// amazonDynamoDB = ctx.getBean(AmazonDynamoDB.class);
		// provider = ctx.getBean(DynamoDBServiceProvider.class);
		
		// Creating Student table
		// Utils.createTable(amazonDynamoDB, "Student");

		student = new Student();
		student.setStudentId("1");
		student.setFirstName("Sam");
		student.setLastName("Smith");
		student.setAge(35);

		
		

		/*
		 * ctx = ApplicationContext.build().build();
		 * 
		 * ctx.registerSingleton(AmazonDynamoDB.class, amazonDynamoDB);
		 * 
		 * ctx.start();
		 * 
		 * // Obtain the provider bean provider =
		 * ctx.getBean(DynamoDBServiceProvider.class);
		 */

		// Obtain DynamoDBService for particular DynamoDB entity
		// dynamoDBService = provider.findOrCreate(Student.class);

	}

	/*
	 * @AfterAll public static void cleanup() {
	 * 
	 * // Clean up Application context after the test if (ctx != null) {
	 * ctx.close(); }
	 * 
	 * }
	 */

	@Test
	public void saveStudentTest() {
		when(dynamoDBService.save(student)).thenReturn(student);
		Student st = client.toBlocking().retrieve(HttpRequest.POST("/createStudent", student), Student.class);
		verify(dynamoDBService).save(student);

	}

	@Test
	public void getOneStudentTest() {
		when(dynamoDBService.get("1", "Smith")).thenReturn(student);
		Student st = client.toBlocking().retrieve(HttpRequest.GET("/getOneStudentDetails/1/Smith"), Student.class);
		verify(dynamoDBService).get("1", "Smith");

	}

	@Test
	public void deleteStudentTest() {

		String st = client.toBlocking().retrieve(HttpRequest.DELETE("/deleteStudent/1/Smith"), String.class);
		verify(dynamoDBService).delete(student);

	}

}
