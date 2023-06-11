package org.example.PDFGenerator;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class PDFGeneratorController {
    private Queue<String> queue;
    private String fileName = "test"; //durch Userdaten ersetzen um Individuelle PDF-Namen zu erstellen
    private Integer fileID = 1;         //durch Userdaten ersetzen um Individuelle PDF-Namen zu erstellen
    public PDFGeneratorController() {
        queue = new LinkedList<>();
    }

    public void addToQueue(String text) {
        queue.add(text);
    }

    public void processQueue() {
        // PDF generation and saving of the PDF
        String pdfPath = generatePDF();
        System.out.println("PDF erstellt und gespeichert unter: " + pdfPath);
    }

    private String generatePDF() {
        //String filePath = "D:/Minor/Distributed_Systems/Semesterprojekt/generatedPDFs/"+fileName+fileID+".pdf"; //eventuell durch getPath methode, damit nicht jeder seinnen Path neu eingeben muss

        String filePath = System.getProperty("user.dir") +fileName+fileID+".pdf";
        System.out.println("Path:" + System.getProperty("user.dir"));
        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            while (!queue.isEmpty()) {
                String text = queue.poll();
                Paragraph paragraph = new Paragraph(text);
                paragraph.setTextAlignment(TextAlignment.LEFT);
                document.add(paragraph);
            }

            document.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath;
    }
}

