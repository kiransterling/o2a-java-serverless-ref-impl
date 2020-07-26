package o2a.java.serverless.ref.impl.service;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;

import o2a.java.serverless.ref.impl.model.Student;

@Singleton
public class DynamoDBService {
	
	private static final Logger logger = LoggerFactory.getLogger(DynamoDBService.class);
	
    //@Inject
	//DynamoDBMapper mapper;
	
	private final DynamoDBMapper mapper;

    public DynamoDBService(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    
    
    public String insertIntoDynamoDB(Student student) {
		mapper.save(student);
		logger.info("Successfully inserted into DynamoDB table");
		return "Successfully inserted into DynamoDB table";

	}
    
    public Student getOneStudentDetails(String studentId, String lastName) {

		return mapper.load(Student.class, studentId, lastName);
	}
    
    public void deleteStudentDetails(Student student) {
		mapper.delete(student);
	}
    
    public void updateStudentDetails(Student student) {
		try {
			mapper.save(student, buildDynamoDBSaveExpression(student));
		} catch (ConditionalCheckFailedException exception) {
			logger.error("invalid data - " + exception.getMessage());
		}
	}
    
    public DynamoDBSaveExpression buildDynamoDBSaveExpression(Student student) {
		DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression();
		Map<String, ExpectedAttributeValue> expected = new HashMap<>();
		expected.put("studentId", new ExpectedAttributeValue(new AttributeValue(student.getStudentId()))
				.withComparisonOperator(ComparisonOperator.EQ));
		saveExpression.setExpected(expected);
		return saveExpression;
	}
    
    
    
    

}
