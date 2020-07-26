package o2a.java.serverless.ref.impl.service;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import o2a.java.serverless.ref.impl.model.Student;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

@Singleton
public class DynamoDBService {

	private static final Logger logger = LoggerFactory.getLogger(DynamoDBService.class);

	public DynamoDbClient dynamoDbClient;

	public DynamoDBService(DynamoDbClient dynamoDbClient) {
		this.dynamoDbClient = dynamoDbClient;
	}

	// Create a DynamoDbEnhancedClient and use the DynamoDbClient object
	DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();

	public String putRecord(Student student) {
		try {
			// Create a DynamoDbTable object
			DynamoDbTable<Student> studentTable = enhancedClient.table("Student", TableSchema.fromBean(Student.class));

			// Put the customer data into a DynamoDB table
			studentTable.putItem(student);
			logger.info("Data inserted successfully");
			return "Data inserted successfully";

		} catch (DynamoDbException e) {
			logger.error(e.getMessage());
			return "Error inserting the record in the table : " + e.getMessage();

		}

	}

	public String getRecord(String studentId, String lastName) {

		try {
			// Create a DynamoDbTable object
			DynamoDbTable<Student> studentTable = enhancedClient.table("Student", TableSchema.fromBean(Student.class));

			// Create a KEY object
			Key key = Key.builder().partitionValue(studentId).sortValue(lastName).build();

			// Get the item by using the key
			Student result = studentTable.getItem(r -> r.key(key));
			logger.info("Student first name is " + result.getFirstName());
			return "Student first name is " + result.getFirstName();

		} catch (DynamoDbException e) {
			logger.error(e.getMessage());
			return "Error getting data  from table : " + e.getMessage();

		}

	}

	public String deleteRecord(String studentId, String lastName) {

		try {
			// Create a DynamoDbTable object
			DynamoDbTable<Student> studentTable = enhancedClient.table("Student", TableSchema.fromBean(Student.class));

			// Create a KEY object
			Key key = Key.builder().partitionValue(studentId).sortValue(lastName).build();

			// Get the item by using the key
			Student result = studentTable.deleteItem(key);
			logger.info("Student " + result.getFirstName() + " deleted successfully");
			return "Student " + result.getFirstName() + " deleted successfully";

		} catch (DynamoDbException e) {
			logger.error("Error deleting studentId : "+e.getMessage());
			return "Error deleting studentId : " + studentId + " , Error : " + e.getMessage();

		}

	}

}
