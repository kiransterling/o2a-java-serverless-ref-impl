package o2a.java.serverless.ref.impl.service;



import com.agorapulse.micronaut.aws.dynamodb.annotation.HashKey;
import com.agorapulse.micronaut.aws.dynamodb.annotation.RangeKey;
import com.agorapulse.micronaut.aws.dynamodb.annotation.Service;
import o2a.java.serverless.ref.impl.model.Student;

@Service(Student.class)
public interface IDynamoDBService {

	Student save(Student student);

	void delete(Student student);

	Student get(@HashKey String studenId, @RangeKey String lastName);

}
