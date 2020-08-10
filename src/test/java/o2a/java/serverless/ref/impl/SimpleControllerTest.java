package o2a.java.serverless.ref.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterAll;
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
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import o2a.java.serverless.ref.impl.model.Student;
import o2a.java.serverless.ref.impl.service.IDynamoDBService;
import o2a.java.serverless.ref.impl.shared.Utils;

@MicronautTest(application = Application.class)
@ExtendWith(LocalstackDockerExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class SimpleControllerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleControllerTest.class);

	//public static ApplicationContext ctx;

	static Student student;

	@Inject
	IDynamoDBService dynamoDBService;

	@Inject
	EmbeddedServer server;
	
	AmazonDynamoDB amazonDynamoDB;

	@Inject
	@Client("/api")
	RxHttpClient client;

	DynamoDBServiceProvider provider;

//	DynamoDBService<Student> dynamoDBService;

	@BeforeAll
	public void  setup() throws InterruptedException {



		// Creating Student table
		Utils.createTable(amazonDynamoDB, "Student");

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
		//dynamoDBService = provider.findOrCreate(Student.class);

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

		Student st=dynamoDBService.save(student);
		
		assertEquals("1",st.getStudentId());
		assertEquals("Sam",st.getFirstName());
		assertEquals("Smith",st.getLastName());
		assertEquals(35,st.getAge());

		//Student st = client.toBlocking().retrieve(HttpRequest.POST("/createStudent", student), Student.class);
		//System.out.println(st.toString());

	}

	@Test
	public void getOneStudentTest() {

		Student st=dynamoDBService.get(student.getStudentId(),student.getLastName());

		assertEquals("1",st.getStudentId());
		assertEquals("Sam",st.getFirstName());
		assertEquals("Smith",st.getLastName());
		assertEquals(35,st.getAge());
		
		//Student st = client.toBlocking().retrieve(HttpRequest.GET("/getOneStudentDetails/1/Smith"), Student.class);
		//System.out.println(st.toString());

	}

	@Test
	public void deleteStudentTest() {

	     dynamoDBService.delete(student);

		//String st = client.toBlocking().retrieve(HttpRequest.DELETE("/deleteStudent/1/Smith"), String.class);
		//System.out.println(st);

	}

}
