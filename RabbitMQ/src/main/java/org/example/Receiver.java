package org.example;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Receiver {
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;

    public Receiver(String host, int port) throws IOException, TimeoutException {
        factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public void startReceiving(String queueName, MessageCallback callback) throws IOException {
        channel.queueDeclare(queueName, true, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received message: " + message);

            try {
                callback.onMessageReceived(message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }

    public void close() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

    public interface MessageCallback {
        void onMessageReceived(String message) throws Exception;
    }
}
