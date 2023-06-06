package org.example.PDFGenerator;

import org.example.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class USerCollector {
    public static void main(String[] args) {

        String query = "SELECT * FROM customer";

        try (
                Connection con = Database.getConnection(30001, "customerdb");
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                ) {

            //ps.setInt(1, 1); //replace ?

            while(rs.next()){
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");

                User user = new User(id, firstName, lastName);

                System.out.println(user);

            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        /*
        rs.close();
        ps.close();
        con.close();
         */

        PDFGeneratorController pdfGenerator = new PDFGeneratorController();

        // Add test data to the queue
        pdfGenerator.addToQueue("Testdaten 1");
        pdfGenerator.addToQueue("Testdaten 2");
        pdfGenerator.addToQueue("Testdaten 3");

        // Process the queue and generate the PDFs
        pdfGenerator.processQueue();
    }
}