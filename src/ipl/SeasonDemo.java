package ipl;

import ipl.models.Team;
import ipl.persistence.PlayerRepository;
import ipl.tournament.PointsTablePrinter;
import ipl.tournament.Season;

import java.util.List;
import java.util.Random;

public class SeasonDemo {
    public static void main(String[] args) {
        List<Team> teams = PlayerRepository.loadTeams();
        Season season = new Season(teams, new Random());
        season.playAll();

        System.out.println("matches = " + season.matchesPlayed() + "/" + season.totalMatches());
        PointsTablePrinter.print(season.getTable());
    }
}