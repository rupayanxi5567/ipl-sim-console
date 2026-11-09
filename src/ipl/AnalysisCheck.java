package ipl;

import ipl.models.Team;
import ipl.persistence.PlayerRepository;
import ipl.simulation.Ball;
import ipl.simulation.Innings;
import ipl.simulation.MatchResult;
import ipl.simulation.MatchSimulator;
import ipl.simulation.Outcome;
import ipl.stats.OverStats;
import ipl.stats.Partnership;
import ipl.stats.PartnershipTracker;

import java.util.List;
import java.util.Random;

public class AnalysisCheck {
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
        Random random = new Random(5);

        for (int i = 0; i < 1000; i++) {
            int a = random.nextInt(teams.size());
            int b;
            do {
                b = random.nextInt(teams.size());
            } while (b == a);

            MatchResult m = MatchSimulator.play(teams.get(a), teams.get(b), random);
            checkInnings(m.getFirst());
            checkInnings(m.getSecond());
        }
        System.out.println("failures = " + failures);
    }

    private static void checkInnings(Innings inn) {
        OverStats stats = new OverStats(inn);
        int overs = stats.getOversPlayed();
        check(stats.runsBetween(1, overs) == inn.getRuns(), "prefix sum != innings runs");

        if (overs >= 3) {
            check(stats.bestBurst(3).runs() == bruteForceBestBurst(stats, overs, 3),
                    "sliding window != brute force");
        }

        List<Partnership> list = PartnershipTracker.compute(inn);
        int runs = 0;
        int balls = 0;
        for (Partnership p : list) {
            runs += p.getRuns();
            balls += p.getBalls();
        }
        check(runs == inn.getRuns(), "partnership runs != innings runs");
        check(balls == inn.getLegalBalls(), "partnership balls != legal balls");

        List<Ball> history = inn.getBalls();
        boolean lastBallWicket = history.get(history.size() - 1).outcome() == Outcome.WICKET;
        int expectedCount = lastBallWicket ? inn.getWickets() : inn.getWickets() + 1;
        check(list.size() == expectedCount, "partnership count " + list.size() + " != " + expectedCount);
    }

    private static int bruteForceBestBurst(OverStats stats, int overs, int k) {
        int best = -1;
        for (int start = 1; start + k - 1 <= overs; start++) {
            best = Math.max(best, stats.runsBetween(start, start + k - 1));
        }
        return best;
    }
}