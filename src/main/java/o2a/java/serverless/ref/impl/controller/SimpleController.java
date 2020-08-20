package o2a.java.serverless.ref.impl.controller;

import javax.validation.Valid;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import o2a.java.serverless.ref.impl.model.Student;
import o2a.java.serverless.ref.impl.service.IDynamoDBService;

@Controller("/student")
@Validated
public class SimpleController {

	private IDynamoDBService dynamoDBService;

	public SimpleController(IDynamoDBService dynamoDBService) {
		this.dynamoDBService = dynamoDBService;
	}

	@Post("/add")
	public Student add(@Body @Valid Student student) {
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