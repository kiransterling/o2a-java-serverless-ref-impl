package o2a.java.serverless.ref.impl.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import o2a.java.serverless.ref.impl.model.Student;
import o2a.java.serverless.ref.impl.service.DynamoDBService;

@Controller("/api")
public class SimpleController {

	private DynamoDBService dynamoDBService;

	public SimpleController(DynamoDBService dynamoDBService) {
		this.dynamoDBService = dynamoDBService;
	}

	@Post(value = "/createStudent", consumes = MediaType.APPLICATION_JSON)
	public String createStudent(@Body Student student) {

		return dynamoDBService.putRecord(student);

	}

	@Get("/getOneStudentDetails/{studentId}/{lastName}")
	public String getOneStudentDetails(@PathVariable String studentId, @PathVariable String lastName) {

		return dynamoDBService.getRecord(studentId, lastName);

	}

	@Delete("/deleteStudent/{studentId}/{lastName}")
	public String deleteStudent(@PathVariable String studentId, @PathVariable String lastName) {

		return dynamoDBService.deleteRecord(studentId, lastName);
	}

}