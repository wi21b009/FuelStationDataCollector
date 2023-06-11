package org.example.DataCollectionReceiver;

import java.util.ArrayList;
import java.util.List;

public class JobData {
    private int userID;
    private int jobID;
    private int numDB;
    private int counter; // Counter variable to keep track of received messages
    private List<String> lines;

    public JobData(int userID, int jobID, int numDB) {
        this.userID = userID;
        this.jobID = jobID;
        this.numDB = numDB;
        this.counter = 0;
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

    public int getNumDB() {
        return numDB;
    }

    public int getCounter() {
        return counter;
    }

    public void incrementCounter() {
        counter++;
    }
}
