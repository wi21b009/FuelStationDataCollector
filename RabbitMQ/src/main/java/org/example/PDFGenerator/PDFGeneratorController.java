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
    private double kwh;
    public PDFGeneratorController(Integer userID, double kwh) {
        queue = new LinkedList<>();
        this.fileID = userID;
        this.kwh = kwh;
    }

    public void addToQueue(String text) {
        queue.add(text);
    }

    public String processQueue() throws IOException {
        // PDF generation and saving of the PDF
        String pdfPath = generatePDF();
        System.out.println("PDF erstellt und gespeichert unter: " + pdfPath);

        return pdfPath;
    }

    public String generatePDF() throws IOException {
        //String filePath = System.getProperty("user.dir") +fileID+".pdf";
        /*if (kwh == 0){

        }*/

        String userDir = System.getProperty("user.dir");
        File file = new File(userDir);
        String parentDir = file.getParent();
        String filePath = parentDir + "/customer" +  fileID + ".pdf";

        int counter = 0;
        //String filePath = System.getProperty("user.dir");
        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            while (!queue.isEmpty()) {
                String text = queue.poll();
                Paragraph paragraph = new Paragraph(text);
                paragraph.setTextAlignment(TextAlignment.LEFT);
                document.add(paragraph);
                if (counter==2){
                    fileID = Integer.parseInt(text);
                    System.out.println(fileID);
                }
                counter++;
            }


            document.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath;
    }


}

