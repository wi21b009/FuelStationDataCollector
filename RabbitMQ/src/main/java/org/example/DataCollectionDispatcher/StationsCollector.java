package org.example.DataCollectionDispatcher;

import org.example.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StationsCollector {
    public static void main(String[] args) {

        String query = "SELECT * FROM station";

        try (
                Connection con = Database.getConnection(30002, "stationdb");
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                ) {

            //ps.setInt(1, 1); //replace ?

            while(rs.next()){
                int id = rs.getInt("id");
                String db_url = rs.getString("db_url");
                Float longitude = rs.getFloat("lng");
                Float latitude = rs.getFloat("lat");

                Stations station = new Stations(id, db_url, latitude, longitude);

                System.out.println(station);

            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        /*
        rs.close();
        ps.close();
        con.close();
         */
    }
}