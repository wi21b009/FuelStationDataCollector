package org.example.PDFGenerator;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.MessageProperties;
import org.example.*;
import org.example.DataCollectionReceiver.DataCollectionReceiver;
import org.example.StationDataCollector.Station;
import org.example.StationDataCollector.StationCollector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class USerCollector {
    // Define the name of the input queue
    private static final String INPUT_QUEUE_NAME = "green";
    // Define the name of the output queue
    private static final String OUTPUT_QUEUE_NAME = "blue";


    public static void main(String[] args) throws IOException {
        String query = "SELECT * FROM customer";
        // Establish connection settings
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(30003);

        try (
                Connection con = Database.getConnection(30001, "customerdb");
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
        ) {

            //ps.setInt(1, 1); //replace ?

            while(rs.next()){
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");

                User user = new User(id, firstName, lastName);

                System.out.println(user);

            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        /*
        rs.close();
        ps.close();
        con.close();
         */

        PDFGeneratorController pdfGenerator = new PDFGeneratorController();

        // Add test data to the queue
        pdfGenerator.addToQueue("Testdaten 1");
        pdfGenerator.addToQueue("Testdaten 2");
        pdfGenerator.addToQueue("Testdaten 3");

        // Process the queue and generate the PDFs
        pdfGenerator.processQueue();

        try (com.rabbitmq.client.Connection connection = factory.newConnection();
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
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


    }
}