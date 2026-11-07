package ipl.tests;

import ipl.persistence.Db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbTest {
    public static void main(String[] args) {
        try (Connection conn = Db.get();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM players")) {
            System.out.println("Connected!");
            if (rs.next()) {
                System.out.println("players = " + rs.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}