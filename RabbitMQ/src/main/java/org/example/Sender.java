package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

//source https://www.rabbitmq.com/getstarted.html

public class Sender {
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;

    public Sender(String host, int port) throws Exception {
        factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public void sendMessage(String queueName, String message) throws Exception {
        channel.queueDeclare(queueName, true, false, false, null);
        channel.basicPublish("", queueName, null, message.getBytes());
        System.out.println("Sent message: " + message);
    }

    public void close() throws Exception {
        channel.close();
        connection.close();
    }
}