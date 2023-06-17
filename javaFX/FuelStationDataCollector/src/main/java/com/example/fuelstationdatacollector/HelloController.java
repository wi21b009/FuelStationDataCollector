/*package com.example.fuelstationdatacollector;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class HelloController {

    @FXML
    private TextField UserID;

    @FXML
    private Button Commit;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label statusLabel;

    private ExecutorService executorService;
    private HttpClient client;

    public HelloController() {
        executorService = Executors.newSingleThreadExecutor();
        client = HttpClient.newHttpClient();
    }

    @FXML
    protected void onClickCommit() {
        String userID = UserID.getText();

        //Building the URI for the POST request
        URI postUri;
        try {
            postUri = new URI("http://localhost:8080/invoices/" + userID);
        } catch (URISyntaxException e) {
            statusLabel.setText("Invalid User-ID");
            return;
        }

        //Building the POST request
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(postUri)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        //Send POST request
        HttpResponse<String> postResponse;
        try {
            System.out.println("Sending POST request...");
            postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("POST response received. Status code: " + postResponse.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            statusLabel.setText("Error sending POST request");
            return;
        }

        // Check if the POST request was successful
        if (postResponse.statusCode() == 200) {
            // POST request succeeded
            statusLabel.setText("POST request successful");

            // Build the URI for the GET request
            URI getUri;
            try {
                getUri = new URI("http://localhost:8080/invoices/" + userID);
            } catch (URISyntaxException e) {
                statusLabel.setText("Invalid User-ID");
                return;
            }

            // Building the GET request
            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(getUri)
                    .GET()
                    .build();

            // Send GET request
            HttpResponse<String> getResponse;
            try {
                System.out.println("Sending GET request...");
                getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
                System.out.println("GET response received. Status code: " + getResponse.statusCode());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                statusLabel.setText("Error sending GET request");
                return;
            }

            // Check if the GET request was successful
            if (getResponse.statusCode() == 200) {
                // GET request succeeded
                String responseText = getResponse.body();
                statusLabel.setText("GET request successful. Response: " + responseText);
                // Hier können Sie weitere Aktionen mit der GET-Antwort durchführen
            } else {
                // GET request failed
                statusLabel.setText("Error retrieving invoice information");
                // Hier können Sie weitere Aktionen bei einem fehlgeschlagenen GET durchführen
            }
        } else {
            // POST request failed
            statusLabel.setText("Error creating invoice");
            // Hier können Sie weitere Aktionen bei einem fehlgeschlagenen POST durchführen
        }
    }
}*/

package com.example.fuelstationdatacollector;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class HelloController {

    @FXML
    private TextField UserID;

    @FXML
    private Button Commit;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label statusLabel;

    private ExecutorService executorService;
    private HttpClient client;

    public HelloController() {
        executorService = Executors.newSingleThreadExecutor();
        client = HttpClient.newHttpClient();
    }

    @FXML
    protected void onClickCommit() {
        String userID = UserID.getText();

        //Building the URI for the POST request
        URI postUri;
        try {
            postUri = new URI("http://localhost:8080/invoices/" + userID);
        } catch (URISyntaxException e) {
            statusLabel.setText("Invalid User-ID");
            return;
        }

        //Building the POST request
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(postUri)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        //Send POST request
        HttpResponse<String> postResponse;
        try {
            System.out.println("Sending POST request...");
            postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("POST response received. Status code: " + postResponse.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            statusLabel.setText("Error sending POST request");
            return;
        }

        // Check if the POST request was successful
        if (postResponse.statusCode() == 200) {
            // POST request succeeded
            statusLabel.setText("POST request successful");

            // Build the URI for the GET request
            URI getUri;
            try {
                getUri = new URI("http://localhost:8080/invoices/" + userID);
            } catch (URISyntaxException e) {
                statusLabel.setText("Invalid User-ID");
                return;
            }

            // Building the GET request
            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(getUri)
                    .GET()
                    .build();

            // Send GET request
            HttpResponse<String> getResponse;
            try {
                System.out.println("Sending GET request...");
                getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
                System.out.println("GET response received. Status code: " + getResponse.statusCode());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                statusLabel.setText("Error sending GET request");
                return;
            }

            // Check if the GET request was successful
            if (getResponse.statusCode() == 200) {
                // GET request succeeded
                String responseText = getResponse.body();
                statusLabel.setText("GET request successful. Response: " + responseText);
                // Hier können Sie weitere Aktionen mit der GET-Antwort durchführen
            } else {
                // GET request failed
                statusLabel.setText("Error retrieving invoice information");
                // Hier können Sie weitere Aktionen bei einem fehlgeschlagenen GET durchführen
            }

            // Clear the UserID text field
            UserID.clear();
        } else {
            // POST request failed
            statusLabel.setText("Error creating invoice");
            // Hier können Sie weitere Aktionen bei einem fehlgeschlagenen POST durchführen
        }
    }

}