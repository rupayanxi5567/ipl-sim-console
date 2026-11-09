package ipl;

import ipl.models.Team;
import ipl.persistence.PlayerRepository;
import ipl.tournament.Fixture;
import ipl.tournament.FixtureGenerator;
import ipl.tournament.FixtureValidator;
import ipl.tournament.Season;
import ipl.tournament.TeamRecord;

import java.util.List;
import java.util.Random;

public class SeasonCheck {
    private static int failures = 0;

    private static void check(boolean ok, String message) {
        if (!ok) {
            failures++;
            if (failures <= 5) {
                System.out.println("FAIL: " + message);
            }
        }
    }

    public static void main(String[] args) {
        List<Team> teams = PlayerRepository.loadTeams();

        List<Fixture> fixtures = FixtureGenerator.generate(teams);
        List<String> problems = FixtureValidator.validate(teams, fixtures);
        System.out.println("fixtures = " + fixtures.size() + ", problems = " + problems.size());
        System.out.println("home/away breaks = " + FixtureValidator.countBreaks(teams, fixtures));

        Random random = new Random(11);
        int seasons = 200;
        double[] totalPoints = new double[teams.size()];

        for (int s = 0; s < seasons; s++) {
            Season season = new Season(teams, random);
            season.playAll();

            int points = 0;
            int played = 0;
            int wins = 0;
            int losses = 0;
            int runsScored = 0;
            int runsConceded = 0;
            int ballsFaced = 0;
            int ballsBowled = 0;

            for (TeamRecord r : season.getTable().getAllRecords()) {
                check(r.getPlayed() == 18, "team played " + r.getPlayed() + " matches");
                points += r.getPoints();
                played += r.getPlayed();
                wins += r.getWon();
                losses += r.getLost();
                runsScored += r.getRunsScored();
                runsConceded += r.getRunsConceded();
                ballsFaced += r.getBallsFaced();
                ballsBowled += r.getBallsBowled();
                totalPoints[teams.indexOf(r.getTeam())] += r.getPoints();
            }

            check(points == 180, "total points " + points);
            check(played == 180, "total played " + played);
            check(wins == losses, "wins != losses");
            check(runsScored == runsConceded, "runs not conserved");
            check(ballsFaced == ballsBowled, "balls not conserved");

            List<TeamRecord> table = season.getTable().standings();
            for (int i = 1; i < table.size(); i++) {
                TeamRecord above = table.get(i - 1);
                TeamRecord below = table.get(i);
                boolean ok = above.getPoints() > below.getPoints()
                        || (above.getPoints() == below.getPoints()
                        && above.netRunRate() >= below.netRunRate());
                check(ok, "standings order broken");
            }
        }

        System.out.println("failures = " + failures);
        System.out.println("average points over " + seasons + " seasons:");
        for (int i = 0; i < teams.size(); i++) {
            System.out.printf("%-26s %.1f%n", teams.get(i).getName(), totalPoints[i] / seasons);
        }
    }
}