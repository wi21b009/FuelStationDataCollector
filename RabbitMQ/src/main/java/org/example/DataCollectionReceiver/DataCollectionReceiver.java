package org.example.DataCollectionReceiver;

import org.example.Receiver;
import org.example.Sender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DataCollectionReceiver implements Receiver.MessageCallback {

    private static final String INPUT_QUEUE_NAME1 = "purple";
    private static final String INPUT_QUEUE_NAME2 = "blue";
    private static final String OUTPUT_QUEUE_NAME = "yellow";

    public static void main(String[] args) throws Exception {

        // Info
        System.out.println("DataCollectionReceiver up and running");

        // Create the receiver and continuously receive and process messages
        Receiver receiver = null;
        try {
            receiver = new Receiver("localhost", 30003);
            receiver.startReceiving(INPUT_QUEUE_NAME1, new DataCollectionReceiver());

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
        String[] input = message.split("\\|");
        int jobID = Integer.parseInt(input[0].trim());
        int userID = Integer.parseInt(input[1].trim());
        int numDB = Integer.parseInt(input[2].trim());

        // Store the received message in jobDataMap
        JobData jobData = new JobData(userID, jobID, numDB);

        // Process messages from StationDataCollector
        Receiver receiver = null;
        try {
            receiver = new Receiver("localhost", 30003);
            receiver.startReceiving(INPUT_QUEUE_NAME2, new DataCollectionReceiverCallback(jobData));

            while (jobData.getCounter() < numDB) {
                try {
                    Thread.sleep(1000); // Wait for 1 second before checking for new messages
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("All lines received for jobID: " + jobID);


            if (jobData.getCounter() == numDB) {
                System.out.println("Sending to output queue.");

                Sender sender = new Sender("localhost", 30003);
                sender.sendMessage(OUTPUT_QUEUE_NAME, jobData.getMessage());

                sender.close();
            } else {
                System.out.println("JobData still missing lines. Waiting.");
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

    private static class DataCollectionReceiverCallback implements Receiver.MessageCallback {
        private final JobData jobData;

        public DataCollectionReceiverCallback(JobData jobData) {
            this.jobData = jobData;
        }

        @Override
        public void onMessageReceived(String message) throws Exception {
            //splitting the message
            String[] lines = message.split("\n");

            // Extract JobID from the last line
            String lastLine = lines[lines.length - 1];
            int jobID = Integer.parseInt(lastLine.replaceAll("\\D+", ""));

            // Extract id, kwh values, and the last number of the port from each line
            for (int i = 0; i < lines.length - 1; i++) {
                int dbNum = 0;
                int id = 0;
                double kwh = 0;
                String line = lines[i];

                //id
                Pattern idPattern = Pattern.compile("id=(\\d+)");
                Matcher idMatcher = idPattern.matcher(line);
                if (idMatcher.find()) {
                    id = Integer.parseInt(idMatcher.group(1));
                }

                //kwh
                Pattern kwhPattern = Pattern.compile("kwh='(\\d+(\\.\\d+)?)'");
                Matcher kwhMatcher = kwhPattern.matcher(line);
                if (kwhMatcher.find()) {
                    kwh = Double.parseDouble(kwhMatcher.group(1));
                }

                //dbNum
                Pattern portPattern = Pattern.compile("port\\s*=\\s*(\\d+)");
                Matcher portMatcher = portPattern.matcher(line);
                if (portMatcher.find()) {
                    String port = portMatcher.group(1);
                    dbNum = Integer.parseInt(port.substring(port.length() - 1));
                }

                //build together string
                String collectedLine = "id = " + id + " | kwh = " + kwh + " | station = " + dbNum;
                jobData.increaseTotal(kwh);
                jobData.addLine(collectedLine);
            }



            jobData.incrementCounter();
            System.out.println("Lines received for jobID: " + jobData.getJobID() + " - " + jobData.getCounter() + "/" + jobData.getNumDB());
        }
    }
}
