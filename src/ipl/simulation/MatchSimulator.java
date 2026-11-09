package ipl.simulation;

import ipl.models.Player;
import ipl.models.Team;

import java.util.List;
import java.util.Random;

public class MatchSimulator {

    public static MatchResult play(Team teamA, Team teamB, Random random) {
        Team firstTeam = random.nextBoolean() ? teamA : teamB;
        Team secondTeam = (firstTeam == teamA) ? teamB : teamA;

        List<Player> firstXI = XiSelector.pickXi(firstTeam);
        List<Player> secondXI = XiSelector.pickXi(secondTeam);

        Innings first = new Innings(firstTeam.getShortName(), firstXI,
                XiSelector.bowlingAttack(secondXI), secondXI, 0, random);
        first.play();

        int target = first.getRuns() + 1;
        Innings second = new Innings(secondTeam.getShortName(), secondXI,
                XiSelector.bowlingAttack(firstXI), firstXI, target, random);
        second.play();

        String summary;
        Team winner;
        if (second.getRuns() >= target) {
            winner = secondTeam;
            summary = secondTeam.getName() + " won by " + (10 - second.getWickets()) + " wickets";
        } else if (second.getRuns() == first.getRuns()) {
            winner = null;
            summary = "Match tied";
        } else {
            winner = firstTeam;
            summary = firstTeam.getName() + " won by " + (first.getRuns() - second.getRuns()) + " runs";
        }
        return new MatchResult(firstTeam, secondTeam, first, second, winner, summary);
    }
}