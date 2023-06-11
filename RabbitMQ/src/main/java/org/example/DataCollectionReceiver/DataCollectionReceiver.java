package org.example.DataCollectionReceiver;

import com.rabbitmq.client.*;

import java.util.ArrayList;
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
                        jobData = new JobData(userID, jobID);
                        jobDataMap.put(jobID, jobData);
                    }

                    // Process the number of databases (numDB) using a for loop
                    for (int i = 0; i < numDB; i++) {
                        final JobData currentJobData = jobData; // Create a final variable
                        DeliverCallback deliverCallback2 = (consumerTag2, delivery2) -> {
                            try {
                                String message2 = new String(delivery2.getBody(), "UTF-8");
                                System.out.println("Received from StationDataCollector: " + message2 + "\n");

                                currentJobData.addLine(message2);

                                channel.basicAck(delivery2.getEnvelope().getDeliveryTag(), false);
                            } catch (Exception ex) {
                                System.err.println("Exception occurred during message delivery: " + ex.getMessage());
                                ex.printStackTrace();
                            }
                        };

                        boolean autoAck = false;
                        channel.basicConsume(INPUT_QUEUE_NAME2, autoAck, deliverCallback2, consumerTag2 -> {
                        });
                    }

                    // Check if JobData has received all expected lines
                    if (jobData.getLines().size() == numDB) {
                        System.out.println("All lines received for jobID: " + jobID);
                        System.out.println("jobData.getLines().size(): " + jobData.getLines().size());
                        System.out.println("numDB: " + numDB);
                        System.out.println(jobData.getMessage());
                        // Remove the completed JobData from the map if desired
                        // jobDataMap.remove(jobID);
                    } else {
                        System.out.println("Lines received for jobID: " + jobID + " - " + jobData.getLines().size() + "/" + numDB);
                    }

                    System.out.println(jobData.getLines().size());
                    System.out.println(numDB);

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

    static class JobData {
        private int userID;
        private int jobID;
        private List<String> lines;

        public JobData(int userID, int jobID) {
            this.userID = userID;
            this.jobID = jobID;
            this.lines = new ArrayList<>();
        }

        public void addLine(String line) {
            lines.add(line);
        }

        public List<String> getLines() {
            return lines;
        }

        public String getMessage() {
            StringBuilder sb = new StringBuilder();
            sb.append("UserID: ").append(userID).append(", JobID: ").append(jobID).append("\n");
            for (String line : lines) {
                String[] parts = line.split(",");
                String id = parts[0].split("=")[1].trim();
                String kwh = parts[1].split("=")[1].trim();
                String port = parts[3].split("=")[1].trim();
                sb.append("ID: ").append(id).append(", kWh: ").append(kwh).append(", Port: ").append(port).append("\n");
            }
            return sb.toString();
        }
    }
}
