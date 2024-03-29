package org.example.PDFGenerator;

import org.example.Database;
import org.example.Receiver;
import org.example.Sender;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserCollector implements Receiver.MessageCallback {
    // Define the name of the input queue
    private static final String INPUT_QUEUE_NAME = "yellow";

    public static void main(String[] args) throws IOException {
        //info
        System.out.println("UserCollector up and running");

        // Create the receiver and continuously receive and process messages
        Receiver receiver = null;
        try {
            receiver = new Receiver("localhost", 30003);
            receiver.startReceiving(INPUT_QUEUE_NAME, new UserCollector());

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
        // Extract UserID value using regular expression
        Pattern pattern = Pattern.compile("UserID: (\\d+)");
        Matcher matcher = pattern.matcher(message);

        int userID = 0;
        if (matcher.find()) {
            userID = Integer.parseInt(matcher.group(1));
            System.out.println("UserID: " + userID);
        } else {
            System.out.println("UserID not found in the string.");
        }

        //query user data
        String path = null;
        User user = query(userID);
        boolean validUser = true;
        if (user == null){
            validUser = false;
            String errorMessage = "User not found with ID: " + userID;
            System.err.println(errorMessage);
            path = "Error";
        }

        if(validUser) {
            System.out.println("User:" + user);

            PDFGeneratorController pdfGenerator = new PDFGeneratorController(userID, user);

            // Add test data to the queue
            pdfGenerator.addToQueue(user.fnLn());
            pdfGenerator.addToQueue(message);
            pdfGenerator.addToQueue(String.valueOf(user.getId()));
            pdfGenerator.addToQueue(LocalDate.now(ZoneId.of("Europe/Berlin")).toString());
            pdfGenerator.addToQueue(String.valueOf(validUser));

            // Process the queue and generate the PDFs
            path = pdfGenerator.processQueue();
        }


        //another break
        Thread.sleep(3000);


        try {
            Sender sender = new Sender("localhost", 30003);
            sender.sendMessage("black", path);
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

    public User query(int userID) throws SQLException {
        User user = null;
        Connection con = Database.getConnection(30001, "customerdb");

        String query = "SELECT * FROM customer WHERE id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int id = rs.getInt("id");

                user = new User(id, firstName, lastName);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        }
        return user;
    }
}
