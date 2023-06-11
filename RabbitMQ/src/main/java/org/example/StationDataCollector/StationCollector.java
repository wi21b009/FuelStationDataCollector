package org.example.StationDataCollector;

import org.example.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StationCollector {

    public static List<Station> queryDatabase(int userID, int port) throws SQLException {
        Connection con = connect(port);
        List<Station> stationList = new ArrayList<Station>();

        String query = "SELECT * FROM charge WHERE customer_id = ?";


        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                Float kwh = rs.getFloat("kwh");
                int custmer_id = rs.getInt("customer_id");

                Station station = new Station(id, kwh, custmer_id, port);
                stationList.add(station);
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

        return stationList;
    }

    public static Connection connect(int port) throws SQLException {
        Connection con = Database.getConnection(port, "stationdb");
        return con;
    }
}
