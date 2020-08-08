package o2a.java.serverless.ref.impl.shared;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

public class Utils {

	private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

	public static String createTable(final AmazonDynamoDB client, final String tableName) throws InterruptedException {
		List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
		attributeDefinitions.add(new AttributeDefinition().withAttributeName("studentId").withAttributeType("S"));
		attributeDefinitions.add(new AttributeDefinition().withAttributeName("lastName").withAttributeType("S"));

		List<KeySchemaElement> keySchema = new ArrayList<>();
		keySchema.add(new KeySchemaElement().withAttributeName("studentId").withKeyType(KeyType.HASH));
		keySchema.add(new KeySchemaElement().withAttributeName("lastName").withKeyType(KeyType.RANGE));
		ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput().withReadCapacityUnits(2L)
				.withWriteCapacityUnits(2L);

		CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
				.withAttributeDefinitions(attributeDefinitions).withKeySchema(keySchema)
				.withProvisionedThroughput(provisionedThroughput);

		DynamoDB dynamoDB = new DynamoDB(client);

		Table table = dynamoDB.createTable(createTableRequest);

		table.waitForActive();

		LOGGER.info("Creating table {}", tableName);
		if (!TableUtils.createTableIfNotExists(client, createTableRequest)) {
			LOGGER.info("Table {} already exists. Nothing to do", tableName);
		}
		return describeTable(client, tableName).getTable().getTableName();
	}

	public static DescribeTableResult describeTable(final AmazonDynamoDB client, final String tableName) {
		return client.describeTable(new DescribeTableRequest().withTableName(tableName));
	}

}
