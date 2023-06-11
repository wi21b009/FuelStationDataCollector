package org.example.StationDataCollector;

import com.rabbitmq.client.*;

import java.util.List;

public class StationDataCollector {

    // Define the name of the input queue
    private static final String INPUT_QUEUE_NAME = "green";
    // Define the name of the output queue
    private static final String OUTPUT_QUEUE_NAME = "blue";

    public static void main(String[] args) throws Exception {

        // Establish connection settings
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(30003);

        //little info
        System.out.println("StationDataCollector up and running");

        // Create a connection and channel
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Declare the input queue
            channel.queueDeclare(INPUT_QUEUE_NAME, true, false, false, null); //> already declared
            // Declare the output queue
            channel.queueDeclare(OUTPUT_QUEUE_NAME, true, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                try {
                    String message = new String(delivery.getBody(), "UTF-8");

                    //short break
                    Thread.sleep(2000);

                    System.out.println(" Received '" + message + "'");

                    //Split the message:
                    String[] input = message.split("\\|");
                    String userIDString = input[0].trim();
                    int userID = Integer.parseInt(userIDString);

                    String stationPart = input[1].trim();
                    int startIndex = stationPart.lastIndexOf(":") + 1; // Find the index of the last ':' and add 1 to get the start index
                    int endIndex = stationPart.indexOf(",", startIndex); // Find the index of the first ',' after the start index
                    int port = Integer.parseInt(stationPart.substring(startIndex, endIndex-1));

                    String jobIDString = input[2].trim();
                    int jobID = Integer.parseInt(jobIDString);

                    //modify the message as wanted
                    List<Station> modifiedMessage = StationCollector.queryDatabase(userID, port);


                    // Convert modified message to a formatted string
                    StringBuilder modifiedMessageBuilder = new StringBuilder();
                    for (Station station : modifiedMessage) {
                        modifiedMessageBuilder.append(station.toString()).append(System.lineSeparator());
                    }
                    String modifiedMessageString = modifiedMessageBuilder.toString() + " | " + jobID;

                    //another break
                    Thread.sleep(3000);

                    // Publish the modified message to the output queue
                    channel.basicPublish("", OUTPUT_QUEUE_NAME,
                            MessageProperties.PERSISTENT_TEXT_PLAIN,
                            modifiedMessageString.getBytes("UTF-8"));


                    System.out.println("Sent message to DataCollectionReceiver:\n" + modifiedMessageString + "\n\n");


                    // Acknowledge the message received from the input queue
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } catch (Exception ex) {
                    System.err.println("Exception occurred during message delivery: " + ex.getMessage());
                    ex.printStackTrace();
                }
            };

            // Create a consumer that waits for messages from the input queue
            boolean autoAck = false; // Set autoAck to false to manually acknowledge messages
            channel.basicConsume(INPUT_QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {});

            // Keep the service running indefinitely
            while (true) {
                try {
                    Thread.sleep(5000); // Wait for 5 second before checking for new messages
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
