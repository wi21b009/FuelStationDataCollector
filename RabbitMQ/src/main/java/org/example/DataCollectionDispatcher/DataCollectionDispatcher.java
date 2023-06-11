package org.example.DataCollectionDispatcher;


import com.rabbitmq.client.*;

import java.util.List;

public class DataCollectionDispatcher {

    private static final String RED_QUEUE_INPUT = "red";
    private static final String GREEN_QUEUE_OUTPUT = "green";
    private static final String PURPLE_QUEUE_OUTPUT = "purple";
    private static int NUM_DATABASES;

    private static int jobID;

    public static void main(String[] args) throws Exception {

        jobID = 1000;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(30003);

        //little info
        System.out.println("DataCollectionDispatcher up and running");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(GREEN_QUEUE_OUTPUT, true, false, false, null);
            channel.queueDeclare(PURPLE_QUEUE_OUTPUT, true, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                try {
                    String message = new String(delivery.getBody(), "UTF-8");

                    System.out.println("Received: " + message);

                    NUM_DATABASES = StationsCollector.getNumDatabase();
                    List<Stations> stationsList = StationsCollector.queryDatabase();

                    for (Stations station : stationsList) {
                        String stationMessage = message + " | " + station.toString() + " | " + jobID;

                        channel.basicPublish("", GREEN_QUEUE_OUTPUT,
                                MessageProperties.PERSISTENT_TEXT_PLAIN,
                                stationMessage.getBytes("UTF-8"));

                        System.out.println("Sent message to StationDataCollector: " + stationMessage);

                        //do a little timeout
                        Thread.sleep(2000);
                    }



                    String receiverMessage = jobID + " | " + message +
                            " | " + NUM_DATABASES;
                    channel.basicPublish("", PURPLE_QUEUE_OUTPUT,
                            MessageProperties.PERSISTENT_TEXT_PLAIN,
                            receiverMessage.getBytes("UTF-8"));

                    jobID++;

                    System.out.println("Sent message to DataCollectionReceiver: " + receiverMessage);

                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } catch (Exception ex) {
                    System.err.println("Exception occurred during message delivery: " + ex.getMessage());
                    ex.printStackTrace();
                }
            };

            boolean autoAck = false;
            channel.basicConsume(RED_QUEUE_INPUT, autoAck, deliverCallback, consumerTag -> {});

            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
