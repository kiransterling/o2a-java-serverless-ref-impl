package o2a.java.serverless.ref.impl.controller;


import javax.inject.Inject;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Delete;
import o2a.java.serverless.ref.impl.model.Student;
import o2a.java.serverless.ref.impl.service.DynamoDBService;


@Controller("/api")
public class SimpleController {

	//@Inject
	//DynamoDBService dynamoDBService;
	
	private final DynamoDBService dynamoDBService;

    public SimpleController(DynamoDBService dynamoDBService) {
        this.dynamoDBService = dynamoDBService;
    }


	@Post(value = "/CreateStudent", consumes = MediaType.APPLICATION_JSON)
	public String createStudent(@Body Student student) {

		return dynamoDBService.insertIntoDynamoDB(student);

	}

	@Get("/getOneStudentDetails/{studentId}/{lastName}")
	public Student getOneStudentDetails(@PathVariable String studentId, @PathVariable String lastName) {

		return dynamoDBService.getOneStudentDetails(studentId, lastName);

	}

	@Put(value = "/updateStudent", consumes = MediaType.APPLICATION_JSON)
	public String updateStudentDetails(@Body Student student) {
		dynamoDBService.updateStudentDetails(student);
		return "Student " + student.getFirstName() + " Updated successfully.";
	}

	@Delete("/deleteStudent/{studentId}/{lastName}")
	public String deleteStudent(@PathVariable String studentId, @PathVariable String lastName) {
		Student student = new Student();
		student.setStudentId(studentId);
		student.setLastName(lastName);

		dynamoDBService.deleteStudentDetails(student);
		return "Student with id " + studentId + " Deleted successfully.";
	}

}