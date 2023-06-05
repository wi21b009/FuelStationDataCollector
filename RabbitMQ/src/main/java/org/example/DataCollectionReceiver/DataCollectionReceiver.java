package org.example.DataCollectionReceiver;

import com.rabbitmq.client.*;

public class DataCollectionReceiver {

    // Define the name of the input queue
    private static final String INPUT_QUEUE_NAME1 = "purple";
    private static final String INPUT_QUEUE_NAME2 = "blue";
    // Define the name of the output queue
    private static final String OUTPUT_QUEUE_NAME = "yellow";

    public static void main(String[] args) throws Exception {

        // Establish connection settings
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(30003);

        // Create a connection and channel
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Declare the input queue
            // channel.queueDeclare(INPUT_QUEUE_NAME, true, false, false, null); > already declared
            // Declare the output queue
            channel.queueDeclare(OUTPUT_QUEUE_NAME, true, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                try {
                    String message = new String(delivery.getBody(), "UTF-8");

                    System.out.println(" Received '" + message + "'");

                    // Modify the message by adding an 's'
                    String modifiedMessage = message + "s";

                    // Publish the modified message to the output queue
                    channel.basicPublish("", OUTPUT_QUEUE_NAME,
                            MessageProperties.PERSISTENT_TEXT_PLAIN,
                            modifiedMessage.getBytes("UTF-8"));

                    System.out.println(" Modified message sent to Queue 2: '" + modifiedMessage + "'");

                    // Acknowledge the message received from the input queue
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } catch (Exception ex) {
                    System.err.println("Exception occurred during message delivery: " + ex.getMessage());
                    ex.printStackTrace();
                }
            };

            // Create a consumer that waits for messages from the input queue
            boolean autoAck = false; // Set autoAck to false to manually acknowledge messages
            channel.basicConsume(INPUT_QUEUE_NAME1, autoAck, deliverCallback, consumerTag -> {});

            // Keep the service running indefinitely
            while (true) {
                try {
                    Thread.sleep(1000); // Wait for 1 second before checking for new messages
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
