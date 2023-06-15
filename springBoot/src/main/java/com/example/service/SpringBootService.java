package com.example.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface SpringBootService {

    void createPDF(Integer id);
    String getPath(Integer id) throws IOException, TimeoutException;

}
