package ipl;

import ipl.models.Team;
import ipl.persistence.PlayerRepository;
import ipl.simulation.Ball;
import ipl.simulation.Innings;
import ipl.simulation.XiSelector;
import ipl.models.Player;

import java.util.List;
import java.util.Random;

public class BallByBallDemo {
    public static void main(String[] args) {
        List<Team> teams = PlayerRepository.loadTeams();
        List<Player> batXI = XiSelector.pickXi(teams.get(0));
        List<Player> bowlXI = XiSelector.pickXi(teams.get(1));

        Innings inn = new Innings(teams.get(0).getShortName(), batXI, XiSelector.bowlingAttack(bowlXI), bowlXI, 0, new Random());

        while (inn.playNextBall()) {
            Ball b = inn.getLastBall();
            System.out.printf("%d.%d  %-22s to %-22s %s   [%d/%d]%n",
                    b.over(), b.ballInOver(),
                    b.bowler().getName(), b.batsman().getName(),
                    b.outcome(), inn.getRuns(), inn.getWickets());
        }
    }
}