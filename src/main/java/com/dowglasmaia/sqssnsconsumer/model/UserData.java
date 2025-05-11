package com.dowglasmaia.sqssnsconsumer.model;


import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@DynamoDbBean
public class UserData {

    @Getter(onMethod_ = @DynamoDbPartitionKey)
    private String identification;
    private String nome;
    private String email;

}