package o2a.java.serverless.ref.impl.service;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import o2a.java.serverless.ref.impl.model.Student;

@Singleton
public class DynamoDBService {

	public static final Logger logger = LoggerFactory.getLogger(DynamoDBService.class);

	private AmazonDynamoDB amazonDynamoDb;

	public DynamoDBService(AmazonDynamoDB amazonDynamoDb) {
		this.amazonDynamoDb = amazonDynamoDb;
	}

	DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDb);

	public String putRecord(Student student) {

		// Put the customer data into a DynamoDB table
		mapper.save(student);
		logger.info("Data inserted successfully");
		return "Data inserted successfully";

	}

	public String getRecord(String studentId, String lastName) {

		return mapper.load(Student.class, studentId, lastName).toString();

	}

	public String deleteRecord(String studentId,String lastName) {

		Student student = new Student();
		student.setStudentId(studentId);
		student.setLastName(lastName);
		
		mapper.delete(student);
		logger.info("Student deleted successfully");
		return "Student deleted successfully";

	}

}
