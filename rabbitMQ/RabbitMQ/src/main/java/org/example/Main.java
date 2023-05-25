package org.example;

import com.rabbitmq.client.*;

public class Main {

    // Define the name of the output queue
    private static final String OUTPUT_QUEUE_NAME = "red";

    public static void main(String[] args) throws Exception {

        // Establish connection settings
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(30003);

        // Create a connection and channel
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Declare the output queue
            channel.queueDeclare(OUTPUT_QUEUE_NAME, true, false, false, null);

            // Create the message
            String Message = "1";

            // Publish the message to the output queue
            channel.basicPublish("", OUTPUT_QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    Message.getBytes("UTF-8"));

            System.out.println(" Stated Queue");

        }
    }
}
