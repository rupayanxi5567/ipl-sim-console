package ipl.stats;

import ipl.simulation.Ball;
import ipl.simulation.Innings;

import java.util.List;

public class OverStats {
    private final int[] runsPerOver;
    private final int[] prefix;

    public record Burst(int startOver, int endOver, int runs) {}

    public  OverStats(Innings innings){
        List<Ball> balls = innings.getBalls();
        int overs = balls.isEmpty() ? 0 : balls.getLast().over() + 1;

        runsPerOver = new int[overs];
        for(Ball b:balls){
            runsPerOver[b.over()] += b.outcome().getRuns();
        }

        prefix = new int[overs+1];
        for(int i=0;i<overs;i++){
            prefix[i+1] = prefix[i] + runsPerOver[i];
        }
    }

    public int getOversPlayed() {
        return runsPerOver.length;
    }

    public int runsInOver(int over) {
        return runsPerOver[over - 1];
    }

    public int runsBetween(int fromOver, int toOver){
        if (fromOver < 1 || toOver > runsPerOver.length || fromOver > toOver) {
            throw new IllegalArgumentException("Bad over range: " + fromOver + "-" + toOver);
        }
        return prefix[toOver] - prefix[fromOver-1];
    }

    public Burst bestBurst(int k) {
        int n = runsPerOver.length;
        if (k <= 0 || n < k) {
            return null;
        }

        int window = 0;
        for (int i = 0; i < k; i++) {
            window += runsPerOver[i];
        }
        int best = window;
        int bestStart = 0;

        for (int i = k; i < n; i++) {
            window += runsPerOver[i] - runsPerOver[i - k];
            if (window > best) {
                best = window;
                bestStart = i - k + 1;
            }
        }
        return new Burst(bestStart + 1, bestStart + k, best);
    }




}
