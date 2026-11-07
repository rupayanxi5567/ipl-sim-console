package ipl.tests;

import ipl.persistence.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PlayerDemo {
    public static void main(String[] args) {
        String sql =
                "SELECT p.name, t.short_name, p.role, bp.batting_avg, bp.strike_rate, bp.source " +
                        "FROM players p " +
                        "JOIN teams t ON t.team_id = p.team_id " +
                        "JOIN batting_profiles bp ON bp.player_id = p.player_id " +
                        "WHERE p.role = ? ORDER BY bp.strike_rate DESC LIMIT 10";

        try (Connection conn = Db.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "BATSMAN");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("%-22s %-5s avg =%6.2f sr =%7.2f (%s)%n",
                            rs.getString("name"),
                            rs.getString("short_name"),
                            rs.getDouble("batting_avg"),
                            rs.getDouble("strike_rate"),
                            rs.getString("source"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}