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

        doNothing().when(springBootService).createPDF(customerId);

        springBootController.createInvoice(customerId);

        verify(springBootService, times(1)).createPDF(customerId);
    }

    @Test
    void createInvoice_NegativeTest() {
        Integer customerId = 1;

        doThrow(RuntimeException.class).when(springBootService).createPDF(customerId);
        assertThrows(RuntimeException.class, () -> springBootController.createInvoice(customerId));

        verify(springBootService, times(1)).createPDF(customerId);
    }

    @Test
    void getInvoice_PositiveTest() throws IOException, TimeoutException {
        Integer customerId = 1;
        String expectedPath = "/path/to/invoice.pdf";

        when(springBootService.getPath(customerId)).thenReturn(expectedPath);

        String path = springBootController.getInvoice(customerId);

        verify(springBootService, times(1)).getPath(customerId);

        assertEquals(expectedPath, path);
    }

    @Test
    void getInvoice_NegativeTest() throws IOException, TimeoutException {
        Integer customerId = 1;

        when(springBootService.getPath(customerId)).thenThrow(IOException.class);

        assertThrows(IOException.class, () -> springBootController.getInvoice(customerId));

        verify(springBootService, times(1)).getPath(customerId);
    }
}

