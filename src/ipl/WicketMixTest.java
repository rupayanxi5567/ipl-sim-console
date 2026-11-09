package ipl;

import ipl.models.Team;
import ipl.persistence.PlayerRepository;
import ipl.simulation.Ball;
import ipl.simulation.BowlerFigures;
import ipl.simulation.Innings;
import ipl.simulation.MatchResult;
import ipl.simulation.MatchSimulator;
import ipl.simulation.WicketType;

import java.util.List;
import java.util.Random;

public class WicketMixTest {
    public static void main(String[] args) {
        List<Team> teams = PlayerRepository.loadTeams();
        Random random = new Random(1);

        int[] counts = new int[WicketType.values().length];
        int totalWickets = 0;
        int teamWickets = 0;
        int bowlerCredited = 0;

        for (int i = 0; i < 100000; i++) {
            int a = random.nextInt(teams.size());
            int b;
            do {
                b = random.nextInt(teams.size());
            } while (b == a);

            MatchResult m = MatchSimulator.play(teams.get(a), teams.get(b), random);
            Innings[] both = {m.getFirst(), m.getSecond()};

            for (Innings inn : both) {
                teamWickets += inn.getWickets();
                for (Ball ball : inn.getBalls()) {
                    if (ball.wicketType() != null) {
                        counts[ball.wicketType().ordinal()]++;
                        totalWickets++;
                    }
                }
                for (BowlerFigures f : inn.getBowlerFigures()) {
                    bowlerCredited += f.getWickets();
                }
            }
        }

        for (WicketType t : WicketType.values()) {
            System.out.printf("%-8s %5.1f%%%n", t, counts[t.ordinal()] * 100.0 / totalWickets);
        }
        System.out.println("team wickets = " + teamWickets
                + ", ball-history wickets = " + totalWickets
                + ", bowler-credited = " + bowlerCredited);
    }
}