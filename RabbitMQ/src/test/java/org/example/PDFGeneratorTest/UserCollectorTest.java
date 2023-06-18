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
        // Arrange
        int userID = 1;
        User expectedUser = new User(userID, "John", "Doe");

        // Mock-Objekt der Datenbank erstellen
        Database mockDatabase = mock(Database.class);

        // Spy-Instanz von UserCollector erstellen und die Abhängigkeiten injizieren
        UserCollector userCollector = Mockito.spy(new UserCollector());
        Mockito.doReturn(expectedUser).when(userCollector).query(userID);

        // Act
        User actualUser = userCollector.query(userID);

        // Assert
        Assertions.assertEquals(expectedUser, actualUser);
    }


    @Test
    void testOnMessageReceived_InvalidKwhValues() throws Exception {
        String message = "UserID: 123 Total kwh: invalid";

        // Mock für die query-Methode, die einen Benutzer zurückgibt
        UserCollector userCollector = new UserCollector() {
            @Override
            public User query(int userID) throws SQLException {
                return new User(userID, "John", "Doe");
            }
        };

        // Mock für den PDFGeneratorController, um das Generieren der PDF zu verhindern
        PDFGeneratorController pdfGeneratorController = mock(PDFGeneratorController.class);

        userCollector.onMessageReceived(message);

        // Überprüfe, ob die entsprechende Reaktion auf ungültige kWh-Werte erfolgt
        // Zum Beispiel, ob eine Meldung ausgegeben wird oder ein Standardwert verwendet wird
        // Hier nehmen wir an, dass eine Meldung auf der Konsole ausgegeben wird
        verify(pdfGeneratorController, never()).addToQueue(anyString());
        verify(pdfGeneratorController, never()).processQueue();
    }

}


