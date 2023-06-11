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

        // Little info
        System.out.println("DataCollectionReceiver up and running");

        // Create a connection and channel
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Declare the input queue
            // channel.queueDeclare(INPUT_QUEUE_NAME, true, false, false, null); > already declared
            // Declare the output queue
            channel.queueDeclare(OUTPUT_QUEUE_NAME, true, false, false, null);

            DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
                try {
                    String message = new String(delivery.getBody(), "UTF-8");
                    System.out.println("Received from DataCollectionDispatcher: " + message);

                    String[] input = message.split("\\|");

                    int JobID = Integer.parseInt(input[0].trim());
                    int UserID = Integer.parseInt(input[1].trim());
                    int numDB = Integer.parseInt(input[2].trim());

                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                    // Process the number of databases (numDB) using a for loop
                    for (int i = 0; i < numDB; i++) {
                        DeliverCallback deliverCallback2 = (consumerTag2, delivery2) -> {
                            try {
                                String message2 = new String(delivery2.getBody(), "UTF-8");
                                System.out.println("Received from StationDataCollector: " + message2);

                                StringSort.StationSort(message2);

                                channel.basicAck(delivery2.getEnvelope().getDeliveryTag(), false);
                            } catch (Exception ex) {
                                System.err.println("Exception occurred during message delivery: " + ex.getMessage());
                                ex.printStackTrace();
                            }
                        };

                        boolean autoAck = false;
                        channel.basicConsume(INPUT_QUEUE_NAME2, autoAck, deliverCallback2, consumerTag2 -> {});
                    }
                } catch (Exception ex) {
                    System.err.println("Exception occurred during message delivery: " + ex.getMessage());
                    ex.printStackTrace();
                }
            };

            boolean autoAck = false;
            channel.basicConsume(INPUT_QUEUE_NAME1, autoAck, deliverCallback1, consumerTag -> {});

            // Keep the service running indefinitely
            while (true) {
                try {
                    Thread.sleep(5000); // Wait for 5 seconds before checking for new messages
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
