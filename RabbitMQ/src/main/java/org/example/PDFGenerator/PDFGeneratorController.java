package org.example.PDFGenerator;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.Queue;

public class PDFGeneratorController {

    private Queue<String> queue;
    private Integer fileID;
    private User user;

    boolean validResult = true;
    public PDFGeneratorController(Integer userID, User user) {
        queue = new LinkedList<>();
        this.fileID = userID;
        this.user = user;
    }

    public void setQueue(Queue<String> queue) {
        this.queue = queue;
    }


    public void addToQueue(String text) {
        queue.add(text);
    }

    public String processQueue() throws IOException {

        String pdfPath = generatePDF();
        return pdfPath;
    }

    public String generatePDF() throws IOException {
        String filePath = generateFilePath();
        generatePdfDocument(filePath);

        if (validResult = false){
            return "Wrong ID!";
        }
        return filePath;
    }

    private String generateFilePath() {
        String userDir = System.getProperty("user.dir");
        File file = new File(userDir);
        String parentDir = file.getParent();
        return parentDir + "/customer" + fileID + ".pdf";
    }

    private void generatePdfDocument(String filePath) throws IOException {
        int counter = 0;
        try (PdfWriter writer = new PdfWriter(filePath);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            while (!queue.isEmpty()) {
                String text = queue.poll();
                Paragraph paragraph = new Paragraph(text);
                paragraph.setTextAlignment(TextAlignment.LEFT);
                document.add(paragraph);
                if (counter == 2) {
                    fileID = Integer.parseInt(text);
                }
                if (counter == 5 && Boolean.parseBoolean(text) == false){
                    validResult = false;
                }
                counter++;
            }
        }
    }
}

