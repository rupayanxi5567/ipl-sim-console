package ipl.persistence;

import ipl.models.BattingProfile;
import ipl.models.BowlingProfile;
import ipl.models.Player;
import ipl.models.Team;
import ipl.models.enums.BattingHand;
import ipl.models.enums.BowlerType;
import ipl.models.enums.BowlingHand;
import ipl.models.enums.Role;
import ipl.utils.RatingCalculator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlayerRepository {
    private static final Random random = new Random();

    private static final long TEAM_BUDGET_LAKHS = 10_000;

    private static final String[] OVERSEAS_COUNTRIES = {
            "Australia", "England", "South Africa", "New Zealand",
            "West Indies", "Afghanistan", "Sri Lanka", "Bangladesh"
    };

    private static final String PLAYER_SQL =
            "SELECT p.player_id, p.name, p.team_id, p.role, p.is_overseas, " +
                    "       bp.batting_avg, bp.strike_rate, bp.balls_faced, bp.fours, bp.sixes, " +
                    "       bp.source AS bat_src, " +
                    "       bw.economy, bw.balls AS bowl_balls, bw.wickets, bw.source AS bowl_src " +
                    "FROM players p " +
                    "LEFT JOIN batting_profiles bp ON bp.player_id = p.player_id " +
                    "LEFT JOIN bowling_profiles bw ON bw.player_id = p.player_id " +
                    "ORDER BY p.player_id";

    private static final String TEAM_SQL = "SELECT team_id, name, short_name FROM teams ORDER BY team_id";

    public static List<Team> loadTeams() {
        Map<Integer, List<Player>> playersByTeam = loadPlayersByTeam();
        List<Team> teams = new ArrayList<>();

        try (Connection conn = Db.get();
             PreparedStatement ps = conn.prepareStatement(TEAM_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int teamId = rs.getInt("team_id");
                List<Player> squad = playersByTeam.getOrDefault(teamId, new ArrayList<>());
                teams.add(new Team(
                        rs.getString("name"),
                        rs.getString("short_name"),
                        TEAM_BUDGET_LAKHS,
                        squad));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load teams", e);
        }
        return teams;
    }

    private static Map<Integer, List<Player>> loadPlayersByTeam() {
        Map<Integer, List<Player>> map = new HashMap<>();

        try (Connection conn = Db.get();
             PreparedStatement ps = conn.prepareStatement(PLAYER_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int teamId = rs.getInt("team_id");
                map.computeIfAbsent(teamId, k -> new ArrayList<>()).add(mapRowToPlayer(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load players", e);
        }
        return map;
    }

    private static Player mapRowToPlayer(ResultSet rs) throws SQLException {
        String id = rs.getString("player_id");
        String name = rs.getString("name");
        String dbRole = rs.getString("role"); // BATSMAN / BOWLER / ALL_ROUNDER / KEEPER
        boolean isOverseas = rs.getBoolean("is_overseas");

        // batting raw stats
        boolean batReal = "REAL".equals(rs.getString("bat_src"));
        double batAvg = rs.getDouble("batting_avg");
        double strikeRate = rs.getDouble("strike_rate");
        int ballsFaced = rs.getInt("balls_faced");
        int fours = rs.getInt("fours");
        int sixes = rs.getInt("sixes");

        // bowling raw stats
        String bowlSrc = rs.getString("bowl_src");
        boolean hasBowling = bowlSrc != null;
        boolean bowlReal = "REAL".equals(bowlSrc);
        double economy = rs.getDouble("economy");
        int bowlBalls = rs.getInt("bowl_balls");
        int wickets = rs.getInt("wickets");

        // batting profile
        BattingHand hand = (random.nextDouble() < 0.80) ? BattingHand.RIGHT : BattingHand.LEFT;
        int aggression = aggressionOf(strikeRate, fours, sixes, ballsFaced, batReal, dbRole);
        int consistency = consistencyOf(batAvg,batReal, dbRole);
        BattingProfile battingProfile = new BattingProfile(batAvg,strikeRate,aggression,consistency,hand);

        // bowling profile
        BowlingProfile bowlingProfile;
        if(hasBowling){
            BowlerType type = (random.nextDouble() < 0.60) ? BowlerType.PACE : BowlerType.SPIN;
            int wicketAbility = wicketAbilityOf(wickets, bowlBalls, bowlReal, dbRole);
            int deathSkill = jitter(wicketAbility);
            int powerPlaySkill = jitter(wicketAbility);
            bowlingProfile = new BowlingProfile(economy, wicketAbility, deathSkill, powerPlaySkill, type);
        }else{
            bowlingProfile = BowlingProfile.none();
        }

        Role finalRole = resolveFinalRole(dbRole, battingProfile, bowlingProfile);

        int fielding = jitter("KEEPER".equals(dbRole) ? 65 : 50);
        int overAll = RatingCalculator.calculateOverall(finalRole,battingProfile, bowlingProfile, fielding);
        long basePrice = priceFor(overAll);
        int age = 18 + random.nextInt(23);
        String nationality = isOverseas ? OVERSEAS_COUNTRIES[random.nextInt(OVERSEAS_COUNTRIES.length)] : "India";

        Player player = new Player(id,name,age,nationality,isOverseas,finalRole,basePrice,battingProfile,bowlingProfile,fielding,overAll);
        return player;
    }

    private static Role resolveFinalRole(String dbRole, BattingProfile batting, BowlingProfile bowling) {
        if (dbRole == null) {
            return Role.BATSMAN;
        }
        switch (dbRole) {
            case "KEEPER":
                return Role.WICKET_KEEPER;
            case "BOWLER":
                return Role.BOWLER;
            case "ALL_ROUNDER": {
                double bat = RatingCalculator.battingScore(batting);
                double bowl = RatingCalculator.bowlingScore(bowling);
                return bat >= bowl ? Role.BATTING_ALL_ROUNDER : Role.BOWLING_ALL_ROUNDER;
            }
            default:
                return Role.BATSMAN;
        }
    }

    private static int jitter(int base) {
        int v = base + random.nextInt(21) - 10; // base ± 10
        return Math.clamp(v, 1, 100);
    }

    private static long priceFor(int overAll) {
        if(overAll >= 85) return 200;
        if(overAll >= 75) return 100;
        if(overAll >= 65) return 50;
        if(overAll >= 55) return 30;
        return 20;
    }

    private static int wicketAbilityOf(int wickets, int bowlBalls, boolean real, String dbRole) {
        if (!real || bowlBalls == 0) {
            return "BOWLER".equals(dbRole) ? 50 : 40;
        }
        double wicketsPer4Overs = wickets * 24.0 / bowlBalls;
        return (int) Math.round(RatingCalculator.normalize(wicketsPer4Overs, 0.4, 2.2));
    }

    private static int consistencyOf(double batAvg, boolean real, String dbRole) {
        if(!real){
            if(dbRole.equals("BATSMAN")){
                return 55;
            }

            if(dbRole.equals("KEEPER")){
                return 50;
            }

            if(dbRole.equals("ALL_ROUNDER")){
                return 45;
            }

            return 25;
        }
        return (int) Math.round(RatingCalculator.normalize(batAvg, 10, 50));
    }

    private static int aggressionOf(double strikeRate, int fours, int sixes, int ballsFaced, boolean real, String dbRole) {
        if(!real || ballsFaced==0){
            if(dbRole.equals("BATSMAN") || dbRole.equals("KEEPER")){
                return 60;
            }

            if(dbRole.equals("ALL_ROUNDER")){
                return 55;
            }

            return 30;
        }
        double boundayPct = (fours+sixes) * 100.0 / ballsFaced;
        double score = RatingCalculator.normalize(strikeRate, 100,250)
                        + RatingCalculator.normalize(boundayPct, 10,35);

        return (int) Math.round(score);
    }
}

