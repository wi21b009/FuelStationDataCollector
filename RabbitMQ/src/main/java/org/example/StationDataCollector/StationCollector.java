package org.example.StationDataCollector;

import org.example.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StationCollector {
    public static void main(String[] args) {

        String query = "SELECT * FROM charge";
        Connection con = null;

        try {
            for (int i = 1; i <= 3; i++) {
                try {
                    if (i == 1) {
                        con = Database.getConnection(30011, "stationdb");
                    } else if (i == 2) {
                        con = Database.getConnection(30012, "stationdb");
                    } else if (i == 3) {
                        con = Database.getConnection(30013, "stationdb");
                    }

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        int id = rs.getInt("id");
                        Float kwh = rs.getFloat("kwh");
                        int custmer_id = rs.getInt("customer_id");

                        Station station = new Station(id, kwh, custmer_id);

                        System.out.println(station);
                    }

                    rs.close();
                    ps.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                } finally {
                    if (con != null && !con.isClosed()) {
                        con.close();
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
