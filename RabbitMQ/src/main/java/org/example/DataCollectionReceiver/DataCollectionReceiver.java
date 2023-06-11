package org.example.DataCollectionReceiver;

import com.rabbitmq.client.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            // Map to store JobData objects based on JobID
            Map<Integer, JobData> jobDataMap = new HashMap<>();

            DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
                try {
                    String message = new String(delivery.getBody(), "UTF-8");
                    System.out.println("Received from DataCollectionDispatcher: " + message);

                    String[] input = message.split("\\|");

                    int jobID = Integer.parseInt(input[0].trim());
                    int userID = Integer.parseInt(input[1].trim());
                    int numDB = Integer.parseInt(input[2].trim());

                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                    // Check if JobID already exists in the map
                    JobData jobData = jobDataMap.get(jobID);
                    if (jobData == null) {
                        jobData = new JobData(userID, jobID, numDB); // Pass the numDB to JobData constructor
                        jobDataMap.put(jobID, jobData);
                    }

                    // Process the number of databases (numDB) using a for loop
                    for (int i = 0; i < numDB; i++) {
                        final JobData currentJobData = jobData; // Create a final variable
                        JobData finalJobData = jobData;
                        DeliverCallback deliverCallback2 = (consumerTag2, delivery2) -> {
                            try {
                                String message2 = new String(delivery2.getBody(), "UTF-8");
                                System.out.println("Received from StationDataCollector: " + message2 + "\n");

                                currentJobData.addLine(message2);

                                channel.basicAck(delivery2.getEnvelope().getDeliveryTag(), false);

                                // Increment the counter after each received message
                                currentJobData.incrementCounter();

                                // Check if all messages have been received
                                if (currentJobData.getCounter() == currentJobData.getNumDB()) {
                                    System.out.println("All lines received for jobID: " + jobID);
                                    System.out.println(finalJobData.getMessage());

                                    // Remove the completed JobData from the map if desired
                                    jobDataMap.remove(jobID);

                                    // Check if JobData has received all expected lines
                                    if (finalJobData.getLines().size() == numDB) {
                                        System.out.println("JobData received all lines. Sending to output queue.");
                                        List<String> lines = finalJobData.getLines();
                                        for (String line : lines) {
                                            channel.basicPublish("", OUTPUT_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, line.getBytes("UTF-8"));
                                        }
                                    } else {
                                        System.out.println("JobData still missing lines. Waiting.");
                                    }
                                } else {
                                    System.out.println("Lines received for jobID: " + jobID + " - " + currentJobData.getCounter() + "/" + currentJobData.getNumDB());
                                }
                            } catch (Exception ex) {
                                System.err.println("Exception occurred during message delivery: " + ex.getMessage());
                                ex.printStackTrace();
                            }
                        };

                        boolean autoAck = false;
                        channel.basicConsume(INPUT_QUEUE_NAME2, autoAck, deliverCallback2, consumerTag2 -> {
                        });
                    }
                    System.out.println("End of for loop");

                } catch (Exception ex) {
                    System.err.println("Exception occurred during message delivery: " + ex.getMessage());
                    ex.printStackTrace();
                }
            };

            boolean autoAck = false;
            channel.basicConsume(INPUT_QUEUE_NAME1, autoAck, deliverCallback1, consumerTag -> {
            });

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