package org.example.DataCollectionReceiver;

import java.util.ArrayList;
import java.util.List;

public class JobData {
    private int userID;
    private int jobID;
    private int numDB; //gives the id of the DB
    private int counter; // Counter variable to keep track of received messages
    private List<String> lines;
    private float total;

    public JobData(int userID, int jobID, int numDB) {
        this.userID = userID;
        this.jobID = jobID;
        this.numDB = numDB;
        this.counter = 0;
        this.lines = new ArrayList<>();
    }

    public void setNumDB(int numDB) {
        this.numDB = numDB;
    }

    public void addLine(String line) {
        lines.add(line);
    }

    public void increaseTotal(double kwH){
        total+= kwH;
    }

    public double getTotal() {
        return total;
    }

    public List<String> getLines() {
        return lines;
    }

    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserID: ").append(userID)
                .append(" | JobID: ").append(jobID)
                .append(" | DB-Nr: ").append(numDB)
                .append("\n");

        for (String line : lines) {
            sb.append(line).append("\n");
        }
        sb.append("\n Total kwh: ").append(total);
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

    public int getJobID() {
        return jobID;
    }
}
