package ipl;

import ipl.models.Team;
import ipl.persistence.PlayerRepository;
import ipl.tournament.BracketNode;
import ipl.tournament.MatchStage;
import ipl.tournament.PlayoffManager;
import ipl.tournament.PlayoffPrinter;
import ipl.tournament.PointsTablePrinter;
import ipl.tournament.Season;

import java.util.List;
import java.util.Random;

public class PlayoffDemo {
    public static void main(String[] args) {
        List<Team> teams = PlayerRepository.loadTeams();
        Season season = new Season(teams, new Random());
        season.playAll();
        PointsTablePrinter.print(season.getTable());

        PlayoffManager playoffs = season.startPlayoffs();
        while (!playoffs.isFinished()) {
            MatchStage stage = playoffs.getCurrentStage();
            BracketNode node = playoffs.playNext();
            System.out.println(stage.getLabel() + ": " + node.getResult().getSummary());
        }
        PlayoffPrinter.printBracket(playoffs);
    }
}