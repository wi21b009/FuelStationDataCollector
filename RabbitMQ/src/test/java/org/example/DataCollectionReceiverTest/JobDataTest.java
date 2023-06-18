package org.example.DataCollectionReceiverTest;


import org.example.DataCollectionReceiver.JobData;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class JobDataTest {

    @Test
    public void testJobID() {
        int jobID = 2;
        JobData jobData = new JobData(1, jobID, 3);
        Assert.assertEquals(jobID, jobData.getJobID());
    }

    @Test
    public void testNumDB() {
        int numDB = 3;
        JobData jobData = new JobData(1, 2, numDB);
        Assert.assertEquals(numDB, jobData.getNumDB());
    }

    @Test
    public void testCounter() {
        JobData jobData = new JobData(1, 2, 3);
        Assert.assertEquals(0, jobData.getCounter());
    }

    @Test
    public void testSetNumDB() {
        JobData jobData = new JobData(1, 2, 3);
        jobData.setNumDB(4);
        Assert.assertEquals(4, jobData.getNumDB());
    }

    @Test
    public void testAddLine() {
        JobData jobData = new JobData(1, 2, 3);
        jobData.addLine("Line 1");
        jobData.addLine("Line 2");
        List<String> lines = jobData.getLines();
        Assert.assertEquals(2, lines.size());
        Assert.assertEquals("Line 1", lines.get(0));
        Assert.assertEquals("Line 2", lines.get(1));
    }

    @Test
    public void testGetMessage() {
        int userID = 1;
        int jobID = 2;
        int numDB = 4;
        double kwH1 = 10.5;
        double kwH2 = 5.3;

        JobData jobData = new JobData(userID, jobID, numDB);

        jobData.addLine("Line 1");
        jobData.addLine("Line 2");

        jobData.increaseTotal(kwH1);
        jobData.increaseTotal(kwH2);

        String expectedMessage = "UserID: 1 | JobID: 2 | DB-Nr: 4\n" +
                "Line 1\n" +
                "Line 2\n" +
                "\n" +
                " Total kwh: " + (kwH1 + kwH2);

        Assert.assertEquals(expectedMessage, jobData.getMessage());
    }


    @Test
    public void testIncrementCounter() {
        // Create a new JobData instance
        JobData jobData = new JobData(1, 1, 1);

        // Get the initial counter value
        int initialCounter = jobData.getCounter();

        // Call the incrementCounter() method
        jobData.incrementCounter();

        // Get the updated counter value
        int updatedCounter = jobData.getCounter();

        // Assert that the counter value has increased by 1
        Assert.assertEquals(initialCounter + 1, updatedCounter);
    }
}