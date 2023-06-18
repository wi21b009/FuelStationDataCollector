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
        controller.setQueue(queue);
    }

    @Test
    public void testAddToQueue() {
        String text = "Test Text";
        controller.addToQueue(text);

        String actualText = queue.poll();

        Assertions.assertEquals(text, actualText);
    }

    @Test
    public void testProcessQueue() {
        String expectedFilePath = "expected/path/to/pdf.pdf";

        PDFGeneratorController controllerSpy = Mockito.spy(controller);
        try {
            Mockito.doReturn(expectedFilePath).when(controllerSpy).generatePDF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String actualFilePath = null;
        try {
            actualFilePath = controllerSpy.processQueue();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(expectedFilePath, actualFilePath);
    }
}
