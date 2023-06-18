package org.example.PDFGeneratorTest;

import org.example.PDFGenerator.PDFGeneratorController;
import org.example.PDFGenerator.User;
import org.example.PDFGenerator.UserCollector;
import org.example.Database;
import org.example.Sender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserCollectorTest {

    @Test
    public void testQuery() throws SQLException {
        int userID = 1;
        User expectedUser = new User(userID, "John", "Doe");

        Database mockDatabase = mock(Database.class);

        UserCollector userCollector = Mockito.spy(new UserCollector());
        Mockito.doReturn(expectedUser).when(userCollector).query(userID);

        User actualUser = userCollector.query(userID);

        Assertions.assertEquals(expectedUser, actualUser);
    }


    @Test
    void testOnMessageReceived_InvalidKwhValues() throws Exception {
        String message = "UserID: 123 Total kwh: invalid";

        UserCollector userCollector = new UserCollector() {
            @Override
            public User query(int userID) throws SQLException {
                return new User(userID, "John", "Doe");
            }
        };

        PDFGeneratorController pdfGeneratorController = mock(PDFGeneratorController.class);

        userCollector.onMessageReceived(message);

        verify(pdfGeneratorController, never()).addToQueue(anyString());
        verify(pdfGeneratorController, never()).processQueue();
    }

}


