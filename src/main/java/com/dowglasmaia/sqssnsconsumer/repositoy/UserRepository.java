package com.dowglasmaia.sqssnsconsumer.repositoy;

import com.dowglasmaia.sqssnsconsumer.model.UserData;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Log4j2
@Repository
public class UserRepository {

    private final static String TABLE_NAME = "user-events";

    private final DynamoDbTable<UserData> userTable;

    public UserRepository(DynamoDbClient dynamoDbClient,
                          @Value("${DYNAMO_TABLE_NAME:user-events}") String tableName) {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();

        this.userTable = enhancedClient.table(tableName, TableSchema.fromBean(UserData.class));
    }

    public void save(UserData user){
        log.info("Salvation user");
        userTable.putItem(user);
    }
}
