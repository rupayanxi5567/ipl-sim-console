package ipl.simulation;

import ipl.models.Player;
import ipl.models.Team;

import java.util.List;
import java.util.Random;

public class MatchSimulator {
    public static MatchResult play(Team teamA,Team teamB, Random random){
        Team tossWon;

        if (random.nextDouble() < 0.5) {
            tossWon = teamA;
        } else {
            tossWon = teamB;
        }

        Team firstTeam = tossWon;
        Team secondTeam = (firstTeam==teamA) ? teamB : teamA;

        List<Player> firstTeamXi = XiSelector.pickXi(firstTeam);
        List<Player> secondTeamXi = XiSelector.pickXi(secondTeam);

        Innings first = new Innings(firstTeam.getShortName(), firstTeamXi, XiSelector.bowlingAttack(secondTeamXi), secondTeamXi, 0, random);
        first.play();

        int target = first.getRuns()+1;

        Innings second = new Innings(secondTeam.getShortName(), secondTeamXi, XiSelector.bowlingAttack(firstTeamXi), firstTeamXi, target, random);
        second.play();

        String summary;

        if (second.getRuns() >= target) {
            summary = secondTeam.getName() + " won by " + (10 - second.getWickets()) + " wickets";
        } else if (second.getRuns() == first.getRuns()) {
            summary = "Match tied";
        } else {
            summary = firstTeam.getName() + " won by " + (first.getRuns() - second.getRuns()) + " runs";
        }
        return new MatchResult(first,second, summary);
    }
}
