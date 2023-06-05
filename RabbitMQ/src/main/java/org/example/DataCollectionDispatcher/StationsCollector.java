package org.example.DataCollectionDispatcher;

import org.example.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class StationsCollector {

    public static Connection connect() throws SQLException {
            Connection con = Database.getConnection(30002, "stationdb");
            return con;
    }


    public static int getNumDatabase() throws SQLException {
        Connection con = connect();

        int numStations = 0;
        String query = "SELECT COUNT(*) FROM station";

        try (
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                numStations = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            con.close();
        }

        return numStations;
    }

    public static List<Stations> queryDatabase() throws SQLException {
        Connection con = connect();
        List<Stations> stationsList = new ArrayList<>();

        String query = "SELECT * FROM station";

        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String db_url = rs.getString("db_url");
                Float longitude = rs.getFloat("lng");
                Float latitude = rs.getFloat("lat");

                Stations station = new Stations(id, db_url, latitude, longitude);
                stationsList.add(station);
            }
        } finally {
            con.close();
        }

        return stationsList;
    }


}