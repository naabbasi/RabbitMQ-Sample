package edu.learn.prod_cons;

import com.devskiller.jfairy.Fairy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

@Slf4j
public class Publisher {
    public static void main(String[] args) throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException, InterruptedException {
        var factory = new ConnectionFactory();
        try (
                var connection = factory.newConnection();
                var channel = connection.createChannel();
        ) {
            factory.setUri("amqp://guest:guest@localhost");
            factory.setConnectionTimeout(300000);
            channel.queueDeclare("my-queue",true, false, false, null);

            long count = 0;
            while(count < 1000){
                var message = "";
                var userAsJSON = new ObjectMapper();
                if(userAsJSON.canSerialize(User.class)) {
                    var fairy = Fairy.create(Locale.SIMPLIFIED_CHINESE);
                    var person = fairy.person();
                    message = userAsJSON.writeValueAsString(new User(count, person.getUsername(), person.getPassword()));
                }
                channel.basicPublish("", "my-queue", null, message.getBytes());
                count++;

                log.info("Publish message : {}", message);
                Thread.sleep(500);
            }
        }
    }
}
