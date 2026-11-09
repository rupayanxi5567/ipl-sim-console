package ipl;

import ipl.models.Team;
import ipl.persistence.PlayerRepository;
import ipl.simulation.MatchResult;
import ipl.simulation.MatchSimulator;

import java.util.List;
import java.util.Random;

public class SanityTest {
    public static void main(String[] args) {
        List<Team> teams = PlayerRepository.loadTeams();
        Random random = new Random(1);
        int n = 10000;

        double totalFirst = 0;
        double totalSecond = 0;
        double totalWickets = 0;
        int chaseWins = 0;
        int ties = 0;
        int over200 = 0;
        int min = Integer.MAX_VALUE;
        int max = 0;

        for (int i = 0; i < n; i++) {
            int a = random.nextInt(teams.size());
            int b;
            do {
                b = random.nextInt(teams.size());
            } while (b == a);

            MatchResult m = MatchSimulator.play(teams.get(a), teams.get(b), random);
            int r1 = m.getFirst().getRuns();
            int r2 = m.getSecond().getRuns();

            totalFirst += r1;
            totalSecond += r2;
            totalWickets += m.getFirst().getWickets();
            if (r2 > r1) {
                chaseWins++;
            } else if (r2 == r1) {
                ties++;
            }
            if (r1 >= 200) {
                over200++;
            }
            min = Math.min(min, r1);
            max = Math.max(max, r1);
        }

        System.out.printf("avg 1st = %.1f, avg 2nd = %.1f%n", totalFirst / n, totalSecond / n);
        System.out.printf("avg wickets (1st) = %.2f%n", totalWickets / n);
        System.out.printf("chase wins = %.1f%%, ties = %d%n", chaseWins * 100.0 / n, ties);
        System.out.printf("200+ scores = %.1f%%, min = %d, max = %d%n", over200 * 100.0 / n, min, max);
    }
}