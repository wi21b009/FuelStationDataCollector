package com.example.fuelstationdatacollector;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.*;

public class HelloController {


    @FXML
    private TextField UserID;

    @FXML
    private Button Commit;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label statusLabel;

    @FXML
    protected void onClickCommit() {
        String userID = UserID.getText();

        //Building the URI for the POST request
        URI postUri;

        try {
            //postUri = new URI("http://localhost:8080/findbyid/" + userID);
            postUri = new URI("http://localhost:8080/invoices/" + userID);
        } catch (URISyntaxException e) {
            statusLabel.setText("Invalid User-ID");
            return;
        }

        //Building the POST request
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(postUri)
                .build();

        //Send POST request
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> postResponse;
        try {
            postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            statusLabel.setText("Error sending POST request");
            return;
        }

        // Check if the POST request was successful
        if (postResponse.statusCode() != 200) {
            statusLabel.setText("Error creating invoice");
            return;
        }

        // Build the URI for the GET request
        URI getUri;
        try {
            //getUri = new URI("http://localhost:8080/findbyid/" + userID);
            getUri = new URI("http://localhost:8080/invoices/" + userID);
        } catch (URISyntaxException e) {
            statusLabel.setText("Invalid user ID");
            return;
        }

        // Build the GET request
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(getUri)
                .GET()
                .build();

        // Send the GET request repeatedly until a response is received
        boolean invoiceCreated = false;
        while (!invoiceCreated) {
            HttpResponse<String> getResponse;
            try {
                getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                statusLabel.setText("Error sending GET request");
                return;
            }

            // Check the response status code
            if (getResponse.statusCode() == 200) {
                // Display the download link and creation time
                String responseText = getResponse.body();
                statusLabel.setText("Invoice created. Download link: " + responseText);
                invoiceCreated = true;
            } else if (getResponse.statusCode() == 404) {
                // Display an error message if no invoice was created
                statusLabel.setText("No invoice created for this user");
                // Wait for a few seconds before trying again
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // Ignore the exception
                }
            } else {
                // Display an error message for any other status code
                statusLabel.setText("Error retrieving invoice information");
                return;
            }
        }

    }
}