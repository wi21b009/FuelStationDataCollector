package com.example.springbootapp.service;

import com.example.service.SpringBootService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SpringBootServiceImplTest {

    @Mock
    private SpringBootService springBootService;

    @Test
    public void testCreatePDF() {
        Integer id = 123;
        Assertions.assertDoesNotThrow(() -> springBootService.createPDF(id));
    }
}
