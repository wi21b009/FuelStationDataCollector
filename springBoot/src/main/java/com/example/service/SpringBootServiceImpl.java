package com.example.service;
import com.rabbitmq.client.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;


@Service
public class SpringBootServiceImpl implements SpringBootService {
    private final static String QUEUE_NAME = "red";
    private final static String QUEUE_Black = "black";
    private StringWrapper messageWrapper = new StringWrapper();
    private Object lock = new Object();

    @Override
    public void createPDF(Integer id) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(30003);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            String message = id.toString();
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));

            System.out.println(" [x] Sent '" + message + "'");
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public synchronized String getPath(Integer id) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(30003);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel(); // Channel deklarieren und initialisieren

        channel.queueDeclare("black", true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String receiveMessage = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + receiveMessage + "'");
            synchronized (lock) {
                messageWrapper.setValue(receiveMessage);
                lock.notify(); // Benachrichtigung senden
                System.out.println("Notification sent");
            }
        };

        channel.basicConsume("black", true, deliverCallback, consumerTag -> {});

        try {
            System.out.println("Waiting for message...");
            synchronized (lock) {
                lock.wait(); // Warten, bis die Benachrichtigung empfangen wird
            }
            System.out.println("Wait ended");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //check if path exist!
        File file = new File(messageWrapper.getValue());
        //File file = new File("'D:\\Minor\\Distributed_Systems\\newVersion/customer3.pdf'");
        if (!file.exists()) {
            throw new IOException("Error: " + messageWrapper.getValue() + "not found");

        }

        return messageWrapper.getValue();
    }

    public class StringWrapper {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}