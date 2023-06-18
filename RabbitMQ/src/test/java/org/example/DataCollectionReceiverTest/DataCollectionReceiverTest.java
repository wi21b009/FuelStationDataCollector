package org.example.DataCollectionReceiverTest;

import org.example.DataCollectionReceiver.DataCollectionReceiver;
import org.example.DataCollectionReceiver.JobData;
import org.example.Receiver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class DataCollectionReceiverTest {

    @Test
    public void testOnMessageReceived() throws Exception {
        // Arrange
        String message = "1 | 2 | 3";
        // Mock-Objekt der Receiver.MessageCallback erstellen
        Receiver.MessageCallback callback = mock(Receiver.MessageCallback.class);

        // Spy-Instanz von DataCollectionReceiver erstellen und die Abhängigkeiten injizieren
        DataCollectionReceiver dataCollectionReceiver = Mockito.spy(new DataCollectionReceiver());
        Mockito.doNothing().when(callback).onMessageReceived(message);

        // Act
        dataCollectionReceiver.onMessageReceived(message);

        // Assert (Beispiel-Assertion)
        //Assertions.assertEquals(expectedResult, actualResult);
    }

    /*@Test
    public void testTotalKwhCalculation() throws Exception {
        // Arrange
        String message = "1 | 2 | 3";
        int expectedTotalKwh = 10;

        // Mock-Objekt für JobData erstellen
        JobData jobData = Mockito.mock(JobData.class);

        // Spy-Instanz von DataCollectionReceiver erstellen und die Abhängigkeiten injizieren
        DataCollectionReceiver dataCollectionReceiver = Mockito.spy(new DataCollectionReceiver());
        Mockito.doReturn(jobData).when(dataCollectionReceiver).createJobData(anyInt(), anyInt(), anyInt());

        // Simulieren, dass die increaseTotal-Methode aufgerufen wird
        doAnswer(invocation -> {
            double kwh = invocation.getArgument(0);
            when(jobData.getTotal()).thenReturn(jobData.getTotal() + kwh);
            return null;
        }).when(jobData).increaseTotal(anyDouble());

        // Act
        dataCollectionReceiver.onMessageReceived(message);

        // Assert
        verify(jobData, times(3)).increaseTotal(anyDouble()); // increaseTotal wird 3-mal aufgerufen (Beispielwert)
        Assertions.assertEquals(expectedTotalKwh, jobData.getTotal());
    }*/
}
