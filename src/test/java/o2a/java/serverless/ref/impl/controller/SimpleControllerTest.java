package o2a.java.serverless.ref.impl.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import o2a.java.serverless.ref.impl.model.Student;
import o2a.java.serverless.ref.impl.service.IDynamoDBService;

@MicronautTest
//@ExtendWith(LocalstackDockerExtension.class)
public class SimpleControllerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleControllerTest.class);

	// @Inject
	// private ApplicationContext ctx;

	Student student;

	@Inject
	private IDynamoDBService dynamoDBService;

	@Inject
	EmbeddedServer server;

	// private AmazonDynamoDB amazonDynamoDB;

	@Inject
	@Client("/student")
	RxHttpClient client;

	// @Inject
	// private DynamoDBServiceProvider provider;

//	DynamoDBService<Student> dynamoDBService;

	@MockBean(IDynamoDBService.class)
	IDynamoDBService dynamoDBService() {
		return mock(IDynamoDBService.class);
	}

	private Student getTestStudentData() {

		if (student != null)
			return student;
		student = new Student();
		student.setStudentId("1");
		student.setFirstName("Sam");
		student.setLastName("Smith");
		student.setAge(35);

		return student;

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

	//@Test
	public void saveStudentTest()  throws MalformedURLException {		
		//when(dynamoDBService.save(getTestStudentData())).thenReturn(getTestStudentData());
		client.toBlocking().exchange(HttpRequest.POST("/add", getTestStudentData()), Student.class);
		verify(dynamoDBService).save(getTestStudentData());

	}

	@Test
	public void getOneStudentTest() throws MalformedURLException {
		when(dynamoDBService.get("1", "Smith")).thenReturn(getTestStudentData());
		Student st = client.toBlocking().retrieve(HttpRequest.GET("/getOneStudentDetails/1/Smith"), Student.class);
		verify(dynamoDBService).get("1", "Smith");

	}

	@Test
	public void deleteStudentTest() throws MalformedURLException {
		String st = client.toBlocking().retrieve(HttpRequest.DELETE("/deleteStudent/1/Smith"), String.class);

		Assertions.assertEquals("Student deleted successfully", st);

	}

}
