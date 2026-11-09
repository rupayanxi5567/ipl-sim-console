package ipl;

import ipl.models.Team;
import ipl.persistence.PlayerRepository;
import ipl.simulation.MatchResult;
import ipl.simulation.MatchSimulator;
import ipl.simulation.ScoreboardPrinter;
import ipl.stats.InningsReport;

import java.util.List;
import java.util.Random;

public class MatchDemo {
    public static void main(String[] args) {
        List<Team> teams = PlayerRepository.loadTeams();
        MatchResult result = MatchSimulator.play(teams.get(0), teams.get(1), new Random());

        ScoreboardPrinter.print(result.getFirst());
        ScoreboardPrinter.print(result.getSecond());
        System.out.println(result.getSummary());

        System.out.println("*********************************************************************************");
        System.out.println("*********************************************************************************");
        System.out.println("*********************************************************************************");

        InningsReport.print(result.getFirst());
        InningsReport.print(result.getSecond());
    }
}