package org.example;

public class Main {

    // Define the name of the output queue
    private static final String OUTPUT_QUEUE_NAME = "red";

    public static void main(String[] args) throws Exception {
        try {
            Sender sender = new Sender("localhost", 30003);
            sender.sendMessage("red", "1");
            sender.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
