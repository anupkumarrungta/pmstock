package org.rungta.pmstock.kiteapp.util;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse;
/*import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;*/

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamoDBService {

    private static DynamoDBMapper mapper;
    private static DynamoDbClient dynamoDbClient;
    private static final Logger logger = LoggerFactory.getLogger(DynamoDBService.class);
    private static Map<String, Boolean> DynamoTableMap = new HashMap<>();

    static {
        try {
            init();
            initializeTableExistenceMap();
        } catch (Exception e) {
            logger.error("Error initializing DynamoDBService: {}", e.getMessage(), e);
        }
    }

    public DynamoDBService() {
    }

    public static synchronized void init() {
        if (dynamoDbClient == null || mapper == null) {
            try {
                String region = Regions.AP_SOUTH_1.getName(); // MUMBAI ap-south-1 is the AWS region
                // Build the DynamoDB client with the credentials fetched from application.properties and region
                AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                        .withRegion(region)
                        .build();

                // Creating a DynamoDBMapper instance with the client
                mapper = new DynamoDBMapper(client);

                // Initialize the DynamoDB client with your AWS credentials
                dynamoDbClient = DynamoDbClient.builder()
                        .region(Region.AP_SOUTH_1) // MUMBAI ap-south-1 is the AWS region
                        .build();
            } catch (Exception e) {
                logger.error("Error initializing DynamoDBService: {}", e.getMessage(), e);
            }
        }
    }

    public static synchronized void initializeTableExistenceMap() {
        try {
            DynamoDbClient client = getDynamoDBClient();
            ListTablesResponse listTablesResponse = client.listTables();
            List<String> tableNames = listTablesResponse.tableNames();

            DynamoTableMap.clear();
            DynamoTableMap = new HashMap<>();
            for (String tableName : tableNames) {
                DynamoTableMap.put(tableName, true);
            }
        } catch (Exception e) {
            logger.error("Error initializing table existence map: {}", e.getMessage(), e);
        }
    }

    public static boolean checkTable(String tableName) {
        try {
            if (!DynamoTableMap.containsKey(tableName) || !DynamoTableMap.get(tableName)) {
                /*
                List<AttributeDefinition> attributeDefinitions = List.of(
                        AttributeDefinition.builder().attributeName("id").attributeType("S").build()
                );

                List<KeySchemaElement> keySchema = List.of(
                        KeySchemaElement.builder().attributeName("id").keyType("HASH").build()
                );

                ProvisionedThroughput throughput = ProvisionedThroughput.builder()
                        .readCapacityUnits(5L)
                        .writeCapacityUnits(5L)
                        .build();

                createTable(tableName, attributeDefinitions, keySchema, throughput);

                 */
                logger.error("No Table found with tableName: {}. Create the table manually in AP_SOUTH_1 MUMBAI region..", tableName);
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("Error checking and creating table: {}", e.getMessage(), e);
            return false;
        }
    }

    /*
    public static void createTable(String tableName, List<AttributeDefinition> attributeDefinitions, List<KeySchemaElement> keySchema, ProvisionedThroughput throughput) {
        try {
            DynamoDbClient client = getDynamoDBClient();
            CreateTableRequest request = CreateTableRequest.builder()
                    .tableName(tableName)
                    .attributeDefinitions(attributeDefinitions)
                    .keySchema(keySchema)
                    .provisionedThroughput(throughput)
                    .build();

            client.createTable(request);
            DynamoTableMap.put(tableName, true);
        } catch (Exception e) {
            logger.error("Error creating table: {}", e.getMessage(), e);
        }
    }
    */

    public static DynamoDBMapper getDynamoDBMapper() {
        if (mapper == null) {
            init();
        }
        return mapper;
    }

    public static DynamoDbClient getDynamoDBClient() {
        if (dynamoDbClient == null) {
            init();
        }
        return dynamoDbClient;
    }

    /*
    public void writeToDynamoDB(ItemDynamoDBObject obj) {
        try {
            mapper.save(obj);
        } catch (DynamoDBMappingException | AmazonDynamoDBException e) {
            e.printStackTrace();
        }
    }

    public ItemDynamoDBObject readFromDynamoDB(String id) {
        try {
            return mapper.load(ItemDynamoDBObject.class, id);
        } catch (DynamoDBMappingException | AmazonDynamoDBException e) {
            e.printStackTrace();
            return null;
        }
    }

     */
}
