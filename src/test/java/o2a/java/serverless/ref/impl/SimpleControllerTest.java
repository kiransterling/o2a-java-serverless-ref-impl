package o2a.java.serverless.ref.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.localstack.LocalStackContainer;

import com.agorapulse.micronaut.aws.dynamodb.DynamoDBService;
import com.agorapulse.micronaut.aws.dynamodb.DynamoDBServiceProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import io.micronaut.context.ApplicationContext;
import io.micronaut.test.annotation.MicronautTest;
import o2a.java.serverless.ref.impl.model.Student;
import o2a.java.serverless.ref.impl.shared.Utils;

@MicronautTest(application = Application.class)
public class SimpleControllerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleControllerTest.class);

	@Rule
	public LocalStackContainer localstack = new LocalStackContainer("latest")
			.withServices(LocalStackContainer.Service.DYNAMODB);

	public ApplicationContext ctx;

	@Before
	public void setup() throws InterruptedException {
		AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClient.builder()
				.withEndpointConfiguration(localstack.getEndpointConfiguration(LocalStackContainer.Service.DYNAMODB))
				.withCredentials(localstack.getDefaultCredentialsProvider()).build();

		// Creating Student table
		Utils.createTable(amazonDynamoDB, "Student");


		ctx = ApplicationContext.build().build();
		ctx.registerSingleton(AmazonDynamoDB.class, amazonDynamoDB);
		ctx.start();
	}

	@After
	public void cleanup() {
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

		dynamoDBService.save(student);

		dynamoDBService.get("1", "Smith");

		dynamoDBService.delete(student);

	}

}
