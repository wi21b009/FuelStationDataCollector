package org.example.DataCollectionDispatcher;

import com.rabbitmq.client.*;

public class DataCollectionDispatcher {

    // Define the name of the input queue
    private static final String RED_QUEUE_INPUT = "red";
    // Define the name of the output queues
    private static final String GREEN_QUEUE_OUTPUT = "green";
    private static final String PURPLE_QUEUE_OUTPUT = "purple";
    // Define the number of databases
    private static final int NUM_DATABASES = 5; //To Do: implement method to collect number of stations

    private static int jobID = 1000;

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

            // Declare the output queues
            channel.queueDeclare(GREEN_QUEUE_OUTPUT, true, false, false, null);
            channel.queueDeclare(PURPLE_QUEUE_OUTPUT, true, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                try {
                    String message = new String(delivery.getBody(), "UTF-8");

                    System.out.println(" Received '" + message + "'");

                    // Send a message for every charging station to the Station Data Collector
                    for (int i = 1; i <= NUM_DATABASES; i++) {
                        // message
                        String stationMessage = "UserID: " + message + " Station " + i;

                        // publish
                        channel.basicPublish("", GREEN_QUEUE_OUTPUT,
                                MessageProperties.PERSISTENT_TEXT_PLAIN,
                                stationMessage.getBytes("UTF-8"));

                        // check
                        System.out.println(" Sent message to StationDataCollector: '" + stationMessage + "'");
                    }

                    // Send a message to the Data Collection Receiver that a new job started
                    String receiverMessage = "Job " + jobID + " started - UserID: " + message + " - Number of databases: " + NUM_DATABASES;
                    channel.basicPublish("", PURPLE_QUEUE_OUTPUT,
                            MessageProperties.PERSISTENT_TEXT_PLAIN,
                            receiverMessage.getBytes("UTF-8"));
                    System.out.println(" Sent message to DataCollectionReceiver: '" + receiverMessage + "'");

                    // Acknowledge the message received from the input queue
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } catch (Exception ex) {
                    System.err.println("Exception occurred during message delivery: " + ex.getMessage());
                    ex.printStackTrace();
                }
            };

            // Create a consumer that waits for messages from the input queue
            boolean autoAck = false; // Set autoAck to false to manually acknowledge messages
            channel.basicConsume(RED_QUEUE_INPUT, autoAck, deliverCallback, consumerTag -> {});

            // add one to the jobID after job is done, so that the new job has other id
            jobID++;

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
