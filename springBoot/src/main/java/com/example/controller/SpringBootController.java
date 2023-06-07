package com.example.controller;

import com.example.service.SpringBootService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringBootController {

    @Autowired
    SpringBootService springBootService;

    @PostMapping("/invoices1/{customerId}")
    public void createInvoice(@PathVariable("customerId") Integer customerId) {
        springBootService.createPDF(customerId);
        System.out.println("CreateInvoice startet with id: " + customerId);
        //dataCollectionDispatcher.startDataGatheringJob(customerId);
    }

    @GetMapping("/invoices/{customerId}")
    public String getInvoice(@PathVariable("customerId") Integer customerId) {
        String path = springBootService.getPath(customerId);
        System.out.println("getInvoice startet with id: " + customerId);
        return "Path/x/y/test.PDF";
    }

}