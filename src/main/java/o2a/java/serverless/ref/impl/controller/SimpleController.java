package o2a.java.serverless.ref.impl.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import o2a.java.serverless.ref.impl.model.Student;
import o2a.java.serverless.ref.impl.service.IDynamoDBService;

@Controller("/api")
public class SimpleController {

	private IDynamoDBService dynamoDBService;

	public SimpleController(IDynamoDBService dynamoDBService) {
		this.dynamoDBService = dynamoDBService;
	}

	@Post(value = "/createStudent", consumes = MediaType.APPLICATION_JSON)
	public Student createStudent(@Body Student student) {

		return dynamoDBService.save(student);

	}

	@Get("/getOneStudentDetails/{studentId}/{lastName}")
	public Student getOneStudentDetails(@PathVariable String studentId, @PathVariable String lastName) {

		return dynamoDBService.get(studentId, lastName);

	}

	@Delete("/deleteStudent/{studentId}/{lastName}")
	public String deleteStudent(@PathVariable String studentId, @PathVariable String lastName) {

		Student studnt = new Student();
		studnt.setStudentId(studentId);
		studnt.setLastName(lastName);

		dynamoDBService.delete(studnt);
		return "Student deleted successfully";

	}

}