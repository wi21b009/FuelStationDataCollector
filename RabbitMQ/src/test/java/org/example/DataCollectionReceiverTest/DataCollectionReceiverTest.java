package org.example.DataCollectionReceiverTest;


import org.example.DataCollectionReceiver.DataCollectionReceiver;
import org.example.DataCollectionReceiver.JobData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

public class DataCollectionReceiverTest {

    //Um diese Methode zu testen, muss die Main geändert werden (unendliches warten auf Antwort)
    @Test
    public void testTotalKwhCalculation() throws Exception {
        String message = "1 | 2 | 3";
        int expectedTotalKwh = 6;

        JobData jobData = Mockito.mock(JobData.class);
        DataCollectionReceiver dataCollectionReceiver = Mockito.spy(new DataCollectionReceiver());

        doAnswer(invocation -> {
            double kwh = invocation.getArgument(0);
            when(jobData.getTotal()).thenReturn(jobData.getTotal() + kwh);
            return null;
        }).when(jobData).increaseTotal(anyDouble());

        dataCollectionReceiver.onMessageReceived(message);

        verify(jobData).increaseTotal(1.0);
        verify(jobData).increaseTotal(2.0);
        verify(jobData).increaseTotal(3.0);
        Assertions.assertEquals(expectedTotalKwh, jobData.getTotal());
    }
}
