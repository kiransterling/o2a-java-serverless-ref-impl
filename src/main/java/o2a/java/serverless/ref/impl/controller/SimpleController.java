package o2a.java.serverless.ref.impl.controller;


import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import o2a.java.serverless.ref.impl.model.Response;
import o2a.java.serverless.ref.impl.model.Student;
import o2a.java.serverless.ref.impl.service.DynamoDBService;

@Controller("/api")
public class SimpleController {

	private DynamoDBService dynamoDBService;

	public SimpleController(DynamoDBService dynamoDBService) {
		this.dynamoDBService = dynamoDBService;
	}
	
	Response resp = new Response();

	@Post(value = "/createStudent", consumes = MediaType.APPLICATION_JSON)
	public HttpResponse<Response> createStudent(@Body Student student) {

		resp.setMessage(dynamoDBService.putRecord(student));
        return HttpResponse.ok(resp).setAttribute("Content-Type", "application/json; charset=utf-8");
		

	}

	@Get("/getOneStudentDetails/{studentId}/{lastName}")
	public HttpResponse<Response> getOneStudentDetails(@PathVariable String studentId, @PathVariable String lastName) {

		resp.setMessage(dynamoDBService.getRecord(studentId, lastName));
        return HttpResponse.ok(resp).setAttribute("Content-Type", "application/json; charset=utf-8");
		

	}

	@Delete("/deleteStudent/{studentId}/{lastName}")
	public HttpResponse<Response> deleteStudent(@PathVariable String studentId, @PathVariable String lastName) {

		resp.setMessage(dynamoDBService.deleteRecord(studentId, lastName));
        return HttpResponse.ok(resp).setAttribute("Content-Type", "application/json; charset=utf-8");
		
	}

}