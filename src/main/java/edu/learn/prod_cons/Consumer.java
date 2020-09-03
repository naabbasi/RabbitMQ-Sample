package edu.learn.prod_cons;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

@Slf4j
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        var factory = new ConnectionFactory();
        try {
            factory.setUri("amqp://guest:guest@localhost");
            factory.setConnectionTimeout(300000);

            var connection = factory.newConnection();
            var channel = connection.createChannel();
            channel.queueDeclare("my-queue", true, false, false, null);

            log.info(" [*] Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                var message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                var jsonToUser = new ObjectMapper();
                var user = jsonToUser.readValue(message.getBytes(), User.class);

                log.info(" [x] Received '{}'", user);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    log.error("InterruptedException: ", e);
                }
            };

            channel.basicConsume("my-queue", true, deliverCallback, consumerTag -> { });

        } catch (URISyntaxException | NoSuchAlgorithmException | KeyManagementException | TimeoutException | IOException e) {
            log.error("Exception: ", e);
        }
    }
}
