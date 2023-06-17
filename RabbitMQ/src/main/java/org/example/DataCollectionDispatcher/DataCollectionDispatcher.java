package org.example.DataCollectionDispatcher;

import org.example.Receiver;
import org.example.Sender;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class DataCollectionDispatcher implements Receiver.MessageCallback {

    private static int NUM_DATABASES;
    private static int jobID = 1000;
    private static boolean newMessageReceived = false;

    public static void main(String[] args) {
        // Info
        System.out.println("DataCollectionDispatcher up and running");

        // Create the receiver and continuously receive and process messages
        Receiver receiver = null;
        try {
            receiver = new Receiver("localhost", 30003);
            receiver.startReceiving("red", new DataCollectionDispatcher());

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
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onMessageReceived(String message) throws Exception {
        newMessageReceived = true;
        processMessage(message);
    }

    private static void processMessage(String message) throws Exception {
        if (newMessageReceived) {
            newMessageReceived = false;
            // DB requests
            NUM_DATABASES = StationsCollector.getNumDatabase();
            List<Stations> stationsList = StationsCollector.queryDatabase();

            // Output message to StationDataCollector
            for (Stations station : stationsList) {
                String stationMessage = message + " | " + station.toString() + " | " + jobID;

                try {
                    Sender sender = new Sender("localhost", 30003);
                    sender.sendMessage("green", stationMessage);
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

            // Output message to DataCollectionReceiver
            String receiverMessage = jobID + " | " + message + " | " + NUM_DATABASES;

            try {
                Sender sender = new Sender("localhost", 30003);
                sender.sendMessage("purple", receiverMessage);
                sender.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // Increase ID for the next job
            jobID++;
        }
    }
}
