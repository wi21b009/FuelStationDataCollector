package org.example.StationDataCollector;

import org.example.Receiver;
import org.example.Sender;

import java.util.List;

public class StationDataCollector implements Receiver.MessageCallback {

    private static int NUM_DATABASES;
    private static int jobID;

    public static void main(String[] args) throws Exception {

        // Info
        System.out.println("StationDataCollector up and running");

        // Create the receiver and continuously receive and process messages
        Receiver receiver = null;
        try {
            receiver = new Receiver("localhost", 30003);
            receiver.startReceiving("green", new StationDataCollector());

            while (true) {
                try {
                    Thread.sleep(5000); // Wait for 5 seconds before checking for new messages
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (receiver != null) {
                try {
                    receiver.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onMessageReceived(String message) throws Exception {
        //Split the message:
        String[] input = message.split("\\|");
        String userIDString = input[0].trim();
        int userID = Integer.parseInt(userIDString);

        String stationPart = input[1].trim();
        int startIndex = stationPart.lastIndexOf(":") + 1; // Find the index of the last ':' and add 1 to get the start index
        int endIndex = stationPart.indexOf(",", startIndex); // Find the index of the first ',' after the start index
        int port = Integer.parseInt(stationPart.substring(startIndex, endIndex - 1));

        String jobIDString = input[2].trim();
        int jobID = Integer.parseInt(jobIDString);

        //modify the message as wanted
        List<Station> stationsList = StationCollector.queryDatabase(userID, port);


        // Convert modified message to a formatted string
        StringBuilder modifiedMessageBuilder = new StringBuilder();
        for (Station station : stationsList) {
            modifiedMessageBuilder.append(station.toString()).append(System.lineSeparator());
        }
        String modifiedMessageString = modifiedMessageBuilder.toString() + " | " + jobID;

        //another break
        Thread.sleep(3000);


        try {
            Sender sender = new Sender("localhost", 30003);
            sender.sendMessage("blue", modifiedMessageString);
            sender.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Timeout
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}