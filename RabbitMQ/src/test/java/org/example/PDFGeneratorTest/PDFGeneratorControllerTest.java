package org.example.PDFGeneratorTest;



import org.example.PDFGenerator.PDFGeneratorController;
import org.example.PDFGenerator.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class PDFGeneratorControllerTest {

    private PDFGeneratorController controller;
    private Queue<String> queue;

    @BeforeEach
    public void setUp() {
        Integer userID = 1;
        User user = new User(1, "John", "Doe"); // Beispielwerte für den Konstruktor von User
        queue = new LinkedList<>();
        controller = new PDFGeneratorController(userID, user);
        controller.setQueue(queue); // Setzen der Queue-Referenz in der PDFGeneratorController-Instanz
    }

    @Test
    public void testAddToQueue() {
        // Arrange
        String text = "Test Text";
        controller.addToQueue(text);

        // Act
        String actualText = queue.poll();

        // Assert
        Assertions.assertEquals(text, actualText);
    }

    @Test
    public void testProcessQueue() {
        // Arrange
        String expectedFilePath = "expected/path/to/pdf.pdf";

        // Mocking generatePDF() method
        PDFGeneratorController controllerSpy = Mockito.spy(controller);
        try {
            Mockito.doReturn(expectedFilePath).when(controllerSpy).generatePDF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Act
        String actualFilePath = null;
        try {
            actualFilePath = controllerSpy.processQueue();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assert
        Assertions.assertEquals(expectedFilePath, actualFilePath);
    }


   /*@Test
    public void testProcessQueue() {
        // Arrange
        String expectedFilePath = "expected/path/to/pdf.pdf";

        // Mocking generatePDF() method
        PDFGeneratorController controllerSpy = Mockito.spy(controller);
        try {
            Mockito.doReturn(expectedFilePath).when(controllerSpy).generatePDF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Act
        String actualFilePath = null;
        try {
            actualFilePath = controllerSpy.processQueue();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assert
        Assertions.assertEquals(expectedFilePath, actualFilePath);
    }*/
}
