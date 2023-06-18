package com.example.springbootapp.controller;

import com.example.controller.SpringBootController;
import com.example.service.SpringBootService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpringBootControllerTest {
    @Mock
    private SpringBootService springBootService;

    @InjectMocks
    private SpringBootController springBootController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createInvoice_PositiveTest() {
        Integer customerId = 1;

        // Mocken der Aufrufe der SpringBootService-Methode
        doNothing().when(springBootService).createPDF(customerId);

        // Methode aufrufen
        springBootController.createInvoice(customerId);

        // Überprüfen, ob die Methode createPDF des SpringBootService aufgerufen wurde
        verify(springBootService, times(1)).createPDF(customerId);
    }

    @Test
    void createInvoice_NegativeTest() {
        Integer customerId = 1;

        // Mocken der Aufrufe der SpringBootService-Methode, die eine Exception wirft
        doThrow(RuntimeException.class).when(springBootService).createPDF(customerId);

        // Test durchführen und auf Exception überprüfen
        assertThrows(RuntimeException.class, () -> springBootController.createInvoice(customerId));

        // Überprüfen, ob die Methode createPDF des SpringBootService aufgerufen wurde
        verify(springBootService, times(1)).createPDF(customerId);
    }

    @Test
    void getInvoice_PositiveTest() throws IOException, TimeoutException {
        Integer customerId = 1;
        String expectedPath = "/path/to/invoice.pdf";

        // Mocken der Aufrufe der SpringBootService-Methode
        when(springBootService.getPath(customerId)).thenReturn(expectedPath);

        // Methode aufrufen
        String path = springBootController.getInvoice(customerId);

        // Überprüfen, ob die Methode getPath des SpringBootService aufgerufen wurde
        verify(springBootService, times(1)).getPath(customerId);

        // Überprüfen, ob der zurückgegebene Pfad dem erwarteten Pfad entspricht
        assertEquals(expectedPath, path);
    }

    @Test
    void getInvoice_NegativeTest() throws IOException, TimeoutException {
        Integer customerId = 1;

        // Mocken der Aufrufe der SpringBootService-Methode, die eine Exception wirft
        when(springBootService.getPath(customerId)).thenThrow(IOException.class);

        // Test durchführen und auf Exception überprüfen
        assertThrows(IOException.class, () -> springBootController.getInvoice(customerId));

        // Überprüfen, ob die Methode getPath des SpringBootService aufgerufen wurde
        verify(springBootService, times(1)).getPath(customerId);
    }
}

