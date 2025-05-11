package com.dowglasmaia.sqssnsconsumer.listener;


import com.dowglasmaia.sqssnsconsumer.model.UserData;
import com.dowglasmaia.sqssnsconsumer.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UserEventListener {

    private final String USER_EVENTS_QUEUE = "user-events-queue";

    private final ObjectMapper mapper = new ObjectMapper();

    private final UserService service;

    public UserEventListener(UserService service) {
        this.service = service;
    }


    @SqsListener(USER_EVENTS_QUEUE) // nome da fila configurada com SNS
    public void receiveMessage(@Payload String message) {
        log.info("Mensagem recebida do SQS: {}", message);

        try {
            JsonNode root = mapper.readTree(message);
            String innerJson = root.get("Message").asText();
            JsonNode eventNode = mapper.readTree(innerJson);
            String data = eventNode.get("data").asText();
            UserData user = mapper.readValue(data, UserData.class);

            service.save(user);

        } catch (Exception e) {
            log.error("Erro ao processar mensagem: {}", e.getMessage());
        }
    }
}
