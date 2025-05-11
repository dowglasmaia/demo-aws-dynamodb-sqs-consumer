# 🚀 demo-aws-dynamodb-sqs-consumer

This project is a **practical example of integration between Amazon SQS and DynamoDB**, using **Java 21 + Spring Boot 3 + Spring Cloud AWS** to build a backend microservice as a message listener.

It is designed to be part of an event-driven architecture on AWS, where multiple services can publish events via SNS → SQS → Consumer.

---

## 🎯 Purpose

To demonstrate a backend service that **consumes SQS messages**, using `spring-cloud-aws-starter-sqs` to:
- Read messages from SQS
- Parse the payload (JSON)
- Persist the data into a DynamoDB table

---

## 📋 Architecture Flow

```plaintext
[ Publisher Service (e.g., API) ]
              |
              v
   SNS (Simple Notification Service)
              |
              v
   SQS (Simple Queue Service)
              |
              v
[ demo-aws-dynamodb-sqs-consumer (listener) ]
              |
              v
     DynamoDB (data persistence)

````

The service listens for SQS events and writes data to DynamoDB.

---

## 💻 Technologies & Libraries

* Java 21
* Spring Boot 3
* Spring Cloud AWS (spring-cloud-aws-starter-sqs)
* AWS SDK for Java v2
* AWS CDK (for infrastructure provisioning)

---

## 🏗️ Project Structure

| Layer           | Description                                                 |
| --------------- | ----------------------------------------------------------- |
| `model`         | Defines the `UserData` entity used for DynamoDB persistence |
| `listener`      | Contains `UserEventListener`, which listens to SQS messages |
| `service`       | Business logic to process events                            |
| `repository`    | DynamoDB persistence layer using AWS Enhanced SDK           |
| `configuration` | Spring Cloud AWS & SQS listener configuration               |

---

## 🔧 Spring Cloud AWS

We use the `spring-cloud-aws-starter-sqs` project to simplify integration with AWS SQS.

Example listener:

```java
@SqsListener("user-events-queue")
public void receiveMessage(String message) {
    // Process SQS message
}
```

Advantages:

* Native listener (no manual polling)
* High availability and fault tolerance
* Simple IAM role-based access for ECS/Fargate tasks

---

## 📥 Example Payload (Received Message)

```json
{
  "Type": "Notification",
  "MessageId": "uuid",
  "TopicArn": "arn:aws:sns:us-east-1:123456789012:user-events-topic",
  "Subject": "User Event: USER_CREATED",
  "Message": "{\"eventType\":\"USER_CREATED\",\"data\":\"{\\\"identification\\\":\\\"id-uuid\\\",\\\"nome\\\":\\\"Dowglas Maia\\\",\\\"email\\\":\\\"dowglas@email.com\\\"}\"}"
}
```

The application extracts the inner JSON from `"Message"`, deserializes it into the `UserData` model and stores it into DynamoDB.

---

## 📝 Example DynamoDB Entity

```java
@DynamoDbBean
public class UserData {
    @DynamoDbPartitionKey
    private String identification;
    private String nome;
    private String email;
}
```

The DynamoDB table uses `identification` as the primary partition key.

---

## ⚙️ Main Configuration Example

`application.yaml`:

```yaml
spring:
  application:
    name: demo-sqs-sns-consumer
  cloud:
    aws:
      region:
        static: us-east-1
    sqs:
      listener:
        max-number-of-messages: 10
        visibility-timeout: 30s
        wait-timeout: 20s
        queue-attributes-resolver:
          strategy: never
```

---

## 🛡️ Security

The listener runs under **IAM Roles for Service Accounts (IRSA)** or ECS Task Roles, allowing:

* `sqs:ReceiveMessage`
* `dynamodb:PutItem`

IAM permissions are provisioned using AWS CDK.

---

## 🐳 Local Run Example

To test locally:

```bash
docker run -p 8080:8080 \
-e AWS_REGION=us-east-1 \
-e AWS_SQS_QUEUE_NAME=user-events-queue \
dowglasmaia/sqs-sns-consumer-user-api:latest
```

---

## 🎯 Summary

This project serves as a **base demo** to:

* Create SQS event-driven consumers
* Build resilient backend services using event-driven patterns
* Integrate Spring Boot microservices with AWS native services through **Spring Cloud AWS**

---




