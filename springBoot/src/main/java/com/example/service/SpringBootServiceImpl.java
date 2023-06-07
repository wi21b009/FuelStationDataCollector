package com.example.service;
import org.springframework.stereotype.Service;

@Service
public class SpringBootServiceImpl implements SpringBootService {

    @Override
    public void createPDF(Integer id) {
        //Data Collection Dipatcher aufrufen
        System.out.println("Test CreatePath");
    }

    @Override
    public String getPath(Integer id) {
       // String path = //Data Collection Dipatcher aufrufen
        System.out.println("TEST getPath!");
        return null; //return path;
    }
}